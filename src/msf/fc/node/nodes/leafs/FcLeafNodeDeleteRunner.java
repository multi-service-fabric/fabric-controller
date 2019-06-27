
package msf.fc.node.nodes.leafs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.nodes.operation.data.NodeCreateDeleteEcRequestBody;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteNodesEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteUpdateNodeEcEntity;
import msf.mfcfc.common.constant.EcNodeOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.NodeBootStatus;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.ErrorResponse;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the Leaf node deletion.
 *
 * @author NTT
 *
 */
public class FcLeafNodeDeleteRunner extends FcAbstractLeafNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeDeleteRunner.class);

  private LeafNodeRequest request;
  private boolean isCreateNodeCancelled;

  private ErrorCode ecResponseStatus = null;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   * @param isCreateNodeCancelled
   *          Node addition cancel flag
   */
  public FcLeafNodeDeleteRunner(LeafNodeRequest request, boolean isCreateNodeCancelled) {

    this.request = request;
    this.isCreateNodeCancelled = isCreateNodeCancelled;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait for node increasing/decreasing process.");
      synchronized (FcNodeManager.getInstance().getFcNodeCreateAndDeleteLockObject()) {
        logger.performance("end wait for node increasing/decreasing process.");
        logger.performance("start wait for node update process.");
        synchronized (FcNodeManager.getInstance().getFcNodeUpdateLockObject()) {
          logger.performance("end wait for node update process.");

          RestResponseBase responseBase = null;
          SessionWrapper sessionWrapper = new SessionWrapper();

          try {
            sessionWrapper.openSession();

            FcNodeDao fcNodeDao = new FcNodeDao();

            List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

            FcNode deleteNode = getDeleteNode(fcNodeList, NodeType.LEAF, Integer.valueOf(request.getNodeId()));

            List<FcNode> borderLeafNodeList = getOtherBorderLeafNodeList(fcNodeList, deleteNode.getNodeId());
            List<FcNode> leafNodes = new ArrayList<>();
            leafNodes.add(deleteNode);

            leafNodes.addAll(borderLeafNodeList);

            List<FcNode> oppositeSpineNodeList = getOppositeNodeList(fcNodeList, NodeType.SPINE);

            sessionWrapper.beginTransaction();
            logger.performance("start get leaf/spine resources lock.");
            FcDbManager.getInstance().getResourceLock(null, null, leafNodes, oppositeSpineNodeList, sessionWrapper);
            logger.performance("end get leaf/spine resources lock.");

            checkDeleteNodeIfs(deleteNode);

            boolean isBorderLeafDelete = LeafType.getEnumFromCode(deleteNode.getLeafNode().getLeafType())
                .equals(LeafType.BORDER_LEAF);

            NodeDeleteNodesEcEntity nodeDeleteNodesEcEntity = deleteOppositeInternalIfs(sessionWrapper, deleteNode,
                oppositeSpineNodeList, isBorderLeafDelete ? InternalNodeType.B_LEAF : InternalNodeType.LEAF);

            NodeDeleteUpdateNodeEcEntity borderLeafDeleteUpdateData = createBorderLeafDeleteUpdateData(
                borderLeafNodeList, isBorderLeafDelete);

            fcNodeDao.delete(sessionWrapper, deleteNode.getNodeInfoId());

            sendLeafNodeDelete(nodeDeleteNodesEcEntity, borderLeafDeleteUpdateData);

            responseBase = responseLeafNodeDeleteData();

            if (isCreateNodeCancelled) {
              nodeNotifyCompleteProcess(deleteNode, NodeBootStatus.CANCEL,
                  new ErrorResponse(ErrorCode.OPERATION_CANCELED, SystemInterfaceType.EXTERNAL));
            }

            sessionWrapper.commitNodeDelete(EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus));
          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();

            FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

          checkNodeDeleteEcError(ecResponseStatus);

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private NodeDeleteUpdateNodeEcEntity createBorderLeafDeleteUpdateData(List<FcNode> borderLeafNodeList,
      boolean isBorderLeafDelete) {
    try {
      logger.methodStart(new String[] { "borderLeafNodeList", "isBorderLeafDelete" },
          new Object[] { borderLeafNodeList, isBorderLeafDelete });
      NodeDeleteUpdateNodeEcEntity nodeDeleteUpdateNodeEcEntity = null;
      if (isBorderLeafDelete) {
        if (!borderLeafNodeList.isEmpty()) {
          nodeDeleteUpdateNodeEcEntity = new NodeDeleteUpdateNodeEcEntity();

          FcNode otherBorderLeafNode = borderLeafNodeList.get(0);
          nodeDeleteUpdateNodeEcEntity.setNodeId(String.valueOf(otherBorderLeafNode.getEcNodeId()));
          nodeDeleteUpdateNodeEcEntity.setNodeType(InternalNodeType.B_LEAF.getMessage());
          int ospfArea = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getOspfArea();
          nodeDeleteUpdateNodeEcEntity.setClusterArea(String.valueOf(ospfArea));
        }
      }
      return nodeDeleteUpdateNodeEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLeafNodeDelete(NodeDeleteNodesEcEntity nodeDeleteNodesEcEntity,
      NodeDeleteUpdateNodeEcEntity borderLeafDeleteUpdateData) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeDeleteNodesEcEntity", "borderLeafDeleteUpdateData" },
          new Object[] { nodeDeleteNodesEcEntity, borderLeafDeleteUpdateData });

      NodeCreateDeleteEcRequestBody nodeCreateDeleteEcRequestBody = new NodeCreateDeleteEcRequestBody();
      nodeCreateDeleteEcRequestBody.setAction(EcNodeOperationAction.DEL_NODE.getMessage());
      NodeDeleteEcEntity delNodeOption = new NodeDeleteEcEntity();
      delNodeOption.setDeleteNodes(nodeDeleteNodesEcEntity);
      delNodeOption.setUpdateNode(borderLeafDeleteUpdateData);
      nodeCreateDeleteEcRequestBody.setDelNodeOption(delNodeOption);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(nodeCreateDeleteEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = null;
      try {
        restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_CREATE_DELETE_REQUEST.getHttpMethod(),
            EcRequestUri.NODE_CREATE_DELETE_REQUEST.getUri(), restRequest, ecControlIpAddress, ecControlPort);
      } catch (MsfException msfException) {
        logger.warn(msfException.getMessage(), msfException);
        if (msfException.getErrorCode() == ErrorCode.EC_CONTROL_TIMEOUT) {

          ecResponseStatus = msfException.getErrorCode();
          return null;
        } else if (msfException.getErrorCode() == ErrorCode.EC_CONNECTION_ERROR) {

          throw msfException;
        } else {

          throw msfException;
        }
      }

      ErrorInternalResponseBody nodeCreateDeleteEcResponseBody = new ErrorInternalResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        try {
          nodeCreateDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
              ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        } catch (MsfException msfException) {
          logger.warn(msfException.getMessage(), msfException);
          if (msfException.getErrorCode() == ErrorCode.EC_CONTROL_ERROR) {

            ecResponseStatus = msfException.getErrorCode();
            return null;
          } else {

            throw msfException;
          }
        }

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), nodeCreateDeleteEcResponseBody.getErrorCode());
        logger.error(errorMsg);

        ecResponseStatus = checkEcEmControlErrorCodeAfterNodeDelete(nodeCreateDeleteEcResponseBody.getErrorCode());
      }

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
