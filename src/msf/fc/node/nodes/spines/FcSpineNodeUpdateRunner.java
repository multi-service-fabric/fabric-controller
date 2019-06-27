
package msf.fc.node.nodes.spines;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.nodes.data.NodeUpdateEcRequestBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.node.nodes.spines.data.SpineNodeUpdateRequestBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the Spine node
 * modification.
 *
 * @author NTT
 *
 */
public class FcSpineNodeUpdateRunner extends FcAbstractSpineNodeRunnerBase {
  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeUpdateRunner.class);

  private SpineNodeRequest request;
  private SpineNodeUpdateRequestBody requestBody;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as arguments.
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcSpineNodeUpdateRunner(SpineNodeRequest request, SpineNodeUpdateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
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

            FcNode updateNode = getUpdateNode(sessionWrapper, fcNodeDao, NodeType.SPINE.getCode(),
                Integer.valueOf(request.getNodeId()));

            sessionWrapper.beginTransaction();

            List<FcNode> spineNodes = new ArrayList<>();
            spineNodes.add(updateNode);

            switch (requestBody.getActionEnum()) {
              case CHG_EQUIPMENT_TYPE:

                responseBase = changeEquipmentTypeProcess(sessionWrapper, updateNode, spineNodes);
                break;

              default:

                throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                    "spine node update action = " + requestBody.getAction());
            }

          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getUpdateNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "nodeType", "nodeId" },
          new Object[] { fcNodeDao, nodeType, nodeId });

      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType, nodeId);

      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcNode");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase changeEquipmentTypeProcess(SessionWrapper sessionWrapper, FcNode updateNode,
      List<FcNode> spineNodes) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "spineNodes" }, new Object[] { updateNode, spineNodes });

      logger.performance("start get spine resources lock.");
      FcDbManager.getInstance().getSpinesLock(spineNodes, sessionWrapper);
      logger.performance("end get spine resources lock.");

      checkChangeEquipmentTypeNodeAfterLock(sessionWrapper, requestBody,
          updateNode.getEquipment().getEquipmentTypeId());

      updateChangeEquipmentType(sessionWrapper, updateNode,
          requestBody.getChangeEquipmentTypeOption().getEquipmentTypeId());

      NodeUpdateEcRequestBody ecRequestBody = createChangeEquipmentNodeData(requestBody);

      sendSpineNodeChangeEquipment(updateNode, ecRequestBody);

      sessionWrapper.commit();

      RestResponseBase responseBase = responseSpineNodeUpdateData();

      return responseBase;
    } catch (MsfException msfException) {
      sessionWrapper.rollback();
      throw msfException;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkChangeEquipmentTypeNodeAfterLock(SessionWrapper sessionWrapper,
      SpineNodeUpdateRequestBody requestBody, Integer nodeEquipmentTypeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "requestBody" },
          new Object[] { sessionWrapper, requestBody });

      getEquipmentForChangeEquipmentType(sessionWrapper,
          Integer.valueOf(requestBody.getChangeEquipmentTypeOption().getEquipmentTypeId()), nodeEquipmentTypeId);
    } finally {
      logger.methodEnd();
    }
  }

  private FcEquipment getEquipmentForChangeEquipmentType(SessionWrapper sessionWrapper, Integer equipmentTypeId,
      Integer nodeEqiopmentTypeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipmentId" }, new Object[] { equipmentTypeId });
      FcEquipmentDao equipmentDao = new FcEquipmentDao();
      FcEquipment equipment = new FcEquipment();
      equipment = equipmentDao.read(sessionWrapper, equipmentTypeId);
      if (equipment == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcEquipment");
      } else if (equipment.getEquipmentTypeId().equals(nodeEqiopmentTypeId)) {

        throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
            "The specified EquipmentTypeID is the same as the EquipmentTypeID of the equipment will be changed");
      }
      return equipment;
    } finally {
      logger.methodEnd();
    }
  }

  private void updateChangeEquipmentType(SessionWrapper sessionWrapper, FcNode updateNode, String equipmentTypeId)
      throws MsfException {
    try {
      logger.methodEnd(new String[] { "fcNode" }, new Object[] { updateNode });
      FcEquipment fcEquipment = new FcEquipment();
      fcEquipment.setEquipmentTypeId(Integer.valueOf(equipmentTypeId));
      updateNode.setEquipment(fcEquipment);
      FcNodeDao dao = new FcNodeDao();
      dao.update(sessionWrapper, updateNode);
    } finally {
      logger.methodEnd();
    }
  }

  private NodeUpdateEcRequestBody createChangeEquipmentNodeData(SpineNodeUpdateRequestBody requestBody) {
    try {
      logger.methodStart(new String[] { "requestBody" }, new Object[] { requestBody });
      NodeUpdateEcRequestBody body = new NodeUpdateEcRequestBody();
      body.setNode(new msf.fc.rest.ec.node.nodes.data.entity.NodeUpdateEcEntity());
      body.getNode().setEquipmentTypeId(requestBody.getChangeEquipmentTypeOption().getEquipmentTypeId());
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendSpineNodeChangeEquipment(FcNode updateNode, NodeUpdateEcRequestBody requestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "requestBody" }, new Object[] { updateNode, requestBody });
      RestRequestBase request = new RestRequestBase();

      request.setRequestBody(JsonUtil.toJson(requestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(
          EcRequestUri.NODE_CHANGE_EQUIPMENT_TYPE.getHttpMethod(),
          EcRequestUri.NODE_CHANGE_EQUIPMENT_TYPE.getUri(String.valueOf(updateNode.getEcNodeId())), request,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody recoverNodeEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = recoverNodeEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseSpineNodeUpdateData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
