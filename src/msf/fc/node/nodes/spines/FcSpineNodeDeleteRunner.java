
package msf.fc.node.nodes.spines;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.nodes.operation.data.NodeCreateDeleteEcRequestBody;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteNodesEcEntity;
import msf.mfcfc.common.constant.EcNodeOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.NodeBootStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.ErrorResponse;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement asynchronous processing in Spine node deletion.
 *
 * @author NTT
 *
 */
public class FcSpineNodeDeleteRunner extends FcAbstractSpineNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeDeleteRunner.class);

  private SpineNodeRequest request;
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
  public FcSpineNodeDeleteRunner(SpineNodeRequest request, boolean isCreateNodeCancelled) {

    this.request = request;
    this.isCreateNodeCancelled = isCreateNodeCancelled;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to node increasing/decreasing process.");
      synchronized (FcNodeManager.getInstance().getFcNodeCreateAndDeleteLockObject()) {
        logger.performance("end wait to node increasing/decreasing process.");
        logger.performance("start wait to node update process.");
        synchronized (FcNodeManager.getInstance().getFcNodeUpdateLockObject()) {
          logger.performance("end wait to node update process.");

          RestResponseBase responseBase = null;
          SessionWrapper sessionWrapper = new SessionWrapper();

          try {
            sessionWrapper.openSession();

            FcNodeDao fcNodeDao = new FcNodeDao();

            List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

            FcNode deleteNode = getDeleteNode(fcNodeList, NodeType.SPINE, Integer.valueOf(request.getNodeId()));

            List<FcNode> spineNodes = new ArrayList<>();
            spineNodes.add(deleteNode);

            List<FcNode> oppositeLeafNodeList = getOppositeNodeList(fcNodeList, NodeType.LEAF);

            sessionWrapper.beginTransaction();
            logger.performance("start get leaf/spine resources lock.");
            FcDbManager.getInstance().getResourceLock(null, null, oppositeLeafNodeList, spineNodes, sessionWrapper);
            logger.performance("end get leaf/spine resources lock.");

            checkDeleteNodeIfs(deleteNode);

            NodeDeleteNodesEcEntity nodeDeleteNodesEcEntity = deleteOppositeInternalIfs(sessionWrapper, deleteNode,
                oppositeLeafNodeList, InternalNodeType.SPINE);

            fcNodeDao.delete(sessionWrapper, deleteNode.getNodeInfoId());

            sendSpineNodeDelete(nodeDeleteNodesEcEntity);

            responseBase = responseSpineNodeDeleteData();

            if (isCreateNodeCancelled) {
              nodeNotifyCompleteProcess(deleteNode, NodeBootStatus.CANCEL,
                  new ErrorResponse(ErrorCode.OPERATION_CANCELED, SystemInterfaceType.EXTERNAL));
            }

            sessionWrapper.commitNodeDelete(EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus));
          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          checkNodeDeleteEcError(ecResponseStatus);

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendSpineNodeDelete(NodeDeleteNodesEcEntity nodeDeleteNodesEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeDeleteNodesEcEntity" }, new Object[] { nodeDeleteNodesEcEntity });

      NodeCreateDeleteEcRequestBody nodeCreateDeleteEcRequestBody = new NodeCreateDeleteEcRequestBody();
      nodeCreateDeleteEcRequestBody.setAction(EcNodeOperationAction.DEL_NODE.getMessage());
      NodeDeleteEcEntity delNodeOption = new NodeDeleteEcEntity();
      delNodeOption.setDeleteNodes(nodeDeleteNodesEcEntity);
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

      ErrorInternalResponseBody nodeCreateDeleteEcResponceBody = new ErrorInternalResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        try {
          nodeCreateDeleteEcResponceBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
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
            restResponseBase.getHttpStatusCode(), nodeCreateDeleteEcResponceBody.getErrorCode());
        logger.error(errorMsg);

        ecResponseStatus = checkEcEmControlErrorCodeAfterNodeDelete(nodeCreateDeleteEcResponceBody.getErrorCode());
      }

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
