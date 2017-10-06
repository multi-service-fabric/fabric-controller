package msf.fc.node.nodes.leafs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeBootStatus;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.NodeManager;
import msf.fc.node.nodes.leafs.data.InternalLeafNodeRequest;
import msf.fc.node.nodes.leafs.data.InternalLeafNodeRequestBody;
import msf.fc.rest.common.JsonUtil;

public class LeafNodeNotifyScenario extends AbstractLeafNodeScenarioBase<InternalLeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(LeafNodeNotifyScenario.class);

  private InternalLeafNodeRequest request;
  private InternalLeafNodeRequestBody requestBody;

  public LeafNodeNotifyScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
  }

  @Override
  protected void checkParameter(InternalLeafNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      InternalLeafNodeRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          InternalLeafNodeRequestBody.class);

      requestBody.validate();

      logger.debug("requestBody=" + request.getRequestBody());

      switch (NodeBootStatus.getEnumFromMessage(requestBody.getStatus())) {
        case SUCCESS:
          setRestIfType(SynchronousType.ASYNC);
          break;
        case CANCEL:
          setRestIfType(SynchronousType.SYNC);
          break;
        default:
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, "UNDEFINED");
      }
      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.performance("start wait to node increasing/decreasing process.");
      synchronized (NodeManager.getInstance().getNodeCreateAndDeleteLockObject()) {
        logger.performance("end wait to node increasing/decreasing process.");
        RestResponseBase responseBase = null;
        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          NodeDao nodeDao = new NodeDao();
          Node node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.LEAF.getCode(),
              Integer.parseInt(request.getNodeId()));

          logger.performance("start get leaf resources lock.");
          sessionWrapper.beginTransaction();
          List<Node> nodes = new ArrayList<>();
          nodes.add(node);
          DbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
          logger.performance("end get leaf resources lock.");

          node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.LEAF.getCode(),
              Integer.parseInt(request.getNodeId()));

          checkProvisioningStatusBeforeNotify(node);

          switch (NodeBootStatus.getEnumFromMessage(requestBody.getStatus())) {
            case SUCCESS:
              node.setProvisioningStatusEnum(ProvisioningStatus.UNSET);
              break;
            case CANCEL:
              node.setProvisioningStatusEnum(ProvisioningStatus.BOOT_FAILED);
              break;
            default:
              throw new MsfException(ErrorCode.UNDEFINED_ERROR, "UNDEFINED");
          }

          nodeDao.update(sessionWrapper, node);

          responseBase = responseLeafNodeNotifyData();

          sessionWrapper.commit();
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        if (NodeBootStatus.getEnumFromMessage(requestBody.getStatus()).equals(NodeBootStatus.SUCCESS)) {
          LeafNodeNotifyRunner leafNodeNotifyRunner = new LeafNodeNotifyRunner(request);
          execAsyncRunner(leafNodeNotifyRunner);
        }

        return responseBase;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeNotifyData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}