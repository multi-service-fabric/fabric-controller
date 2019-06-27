
package msf.fc.services.priorityroutes.scenario.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityReadResponseBody;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityRequest;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.entity.PrioritySystemGroupReadResponseEntity;

/**
 * Implementation class for the priority node group information acquisition.
 *
 * @author NTT
 *
 */
public class FcNodeGroupPriorityReadScenario extends FcAbstractNodeGroupPriorityScenarioBase<NodeGroupPriorityRequest> {

  @SuppressWarnings("unused")
  private NodeGroupPriorityRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeGroupPriorityReadScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcNodeGroupPriorityReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(NodeGroupPriorityRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

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
        FcNodeDao fcNodeDao = new FcNodeDao();

        List<FcNode> fcNodes = fcNodeDao.readList(sessionWrapper);

        responseBase = responseNodeGroupPriorityReadData(fcNodes);

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

  private RestResponseBase responseNodeGroupPriorityReadData(List<FcNode> fcNodes) throws MsfException {
    try {
      logger.methodStart();
      List<String> leafNodeIdList = new ArrayList<>();
      List<String> spineNodeIdList = new ArrayList<>();
      for (FcNode fcNode : fcNodes) {
        if (fcNode.getIsPriorityNodeGroupMember()) {

          switch (fcNode.getNodeTypeEnum()) {
            case LEAF:
              leafNodeIdList.add(String.valueOf(fcNode.getNodeId()));
              break;

            case SPINE:
              spineNodeIdList.add(String.valueOf(fcNode.getNodeId()));
              break;

            default:

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "Illegal parameter. nodeType = " + fcNode.getNodeType());
          }
        }
      }

      NodeGroupPriorityReadResponseBody body = new NodeGroupPriorityReadResponseBody();
      PrioritySystemGroupReadResponseEntity nodes = new PrioritySystemGroupReadResponseEntity();
      nodes.setLeafNodeIdList(leafNodeIdList);
      nodes.setSpineNodeIdList(spineNodeIdList);
      body.setNodes(nodes);
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }
}
