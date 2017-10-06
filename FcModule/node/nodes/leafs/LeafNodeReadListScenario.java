package msf.fc.node.nodes.leafs;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.nodes.leafs.data.LeafNodeReadDetailListResponseBody;
import msf.fc.node.nodes.leafs.data.LeafNodeReadListResponseBody;
import msf.fc.node.nodes.leafs.data.LeafNodeRequest;

public class LeafNodeReadListScenario extends AbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(LeafNodeReadListScenario.class);

  private LeafNodeRequest request;

  public LeafNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

      this.request = request;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.readList(sessionWrapper, request.getClusterId(), NodeType.LEAF.getCode());

        if (nodes.isEmpty()) {
          checkSwCluster(sessionWrapper, request.getClusterId());
        }

        responseBase = responseLeafeNodeReadListData(nodes, request.getFormat());

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafeNodeReadListData(List<Node> nodes, String format) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        LeafNodeReadDetailListResponseBody body = new LeafNodeReadDetailListResponseBody();
        body.setLeafList(getLeafEntities(nodes));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        LeafNodeReadListResponseBody body = new LeafNodeReadListResponseBody();
        body.setLeafNodeIdList(getLeafNodeIdList(nodes));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}