
package msf.fc.node.interfaces.ifmaintenance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.interfaces.status.data.InterfaceChangeStateEcRequestBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.ifmaintenance.data.InterfaceChangeStateRequest;
import msf.mfcfc.node.interfaces.ifmaintenance.data.InterfaceChangeStateRequestBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the interface blockade
 * status modification.
 *
 * @author NTT
 *
 */
public class FcInterfaceMaintenanceUpdateRunnner extends FcAbstractInterfaceMaintenanceRunnerBase {

  private MsfLogger logger = MsfLogger.getInstance(FcInterfaceMaintenanceUpdateRunnner.class);

  private InterfaceChangeStateRequest request;
  private InterfaceChangeStateRequestBody requestBody;

  /**
   * Constructor.
   *
   * @param request
   *          Request for the IF blockade status modification.
   * @param requestBody
   *          Request body for the IF blockade status modification.
   */
  public FcInterfaceMaintenanceUpdateRunnner(InterfaceChangeStateRequest request,
      InterfaceChangeStateRequestBody requestBody) {
    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper sessionWrapper = new SessionWrapper();

      RestResponseBase responseBase = null;
      try {
        sessionWrapper.openSession();
        FcNodeDao nodeDao = new FcNodeDao();

        FcNode fcNode = getNode(sessionWrapper, nodeDao, request.getFabricTypeEnum(),
            Integer.valueOf(request.getNodeId()));
        List<FcNode> nodes = new ArrayList<>();
        nodes.add(fcNode);

        sessionWrapper.beginTransaction();

        switch (request.getFabricTypeEnum()) {
          case LEAF:
            logger.performance("start get leaf resources lock.");
            FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
            logger.performance("end get leaf resources lock.");
            break;
          case SPINE:
            logger.performance("start get spine resources lock.");
            FcDbManager.getInstance().getSpinesLock(nodes, sessionWrapper);
            logger.performance("end get spine resources lock");
            break;
          default:

            throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                "NodeType = " + NodeType.getEnumFromCode(fcNode.getNodeType()).getSingularMessage());
        }

        logger.performance("start wait for IF state change process.");
        synchronized (FcNodeManager.getInstance().getFcIfStateChangeLockObject()) {
          logger.performance("end wait for IF state change process.");

          checkIf(sessionWrapper, request);

          sendRequest(request, requestBody, fcNode);

          responseBase = creatIfBlockageStatusUpdateAsyncData();

          sessionWrapper.rollback();

          return responseBase;
        }
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, NodeType nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "nodeType", "nodeId" },
          new Object[] { fcNodeDao, nodeType, nodeId });
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType.getCode(), nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. Node Id = " + nodeId);
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkIf(SessionWrapper sessionWrapper, InterfaceChangeStateRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      switch (request.getIfTypeEnum()) {
        case PHYSICAL_IF:
          FcPhysicalIfDao physicalIfDao = new FcPhysicalIfDao();
          FcPhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
              Integer.valueOf(request.getNodeId()), request.getIfId());
          if (physicalIf == null) {
            String errMsg = "The specified physical IF does not exist. Physical If Id = " + request.getIfId();
            logger.error(errMsg);
            throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errMsg);
          }
          break;
        case LAG_IF:
          FcLagIfDao lagIfDao = new FcLagIfDao();
          FcLagIf lagIf = lagIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
              Integer.valueOf(request.getNodeId()), Integer.valueOf(request.getIfId()));
          if (lagIf == null) {
            String errMsg = "The specified Lag IF does not exist. Lag If Id = " + request.getIfId();
            logger.error(errMsg);
            throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errMsg);
          }
          break;
        case BREAKOUT_IF:
          FcBreakoutIfDao breakoutIfDao = new FcBreakoutIfDao();
          FcBreakoutIf breakoutIf = breakoutIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
              Integer.valueOf(request.getNodeId()), request.getIfId());
          if (breakoutIf == null) {
            String errMsg = "The specified Breakout IF does not exist. Breakout If Id = " + request.getIfId();
            logger.error(errMsg);
            throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errMsg);
          }
          break;
        default:
          String errMsg = "The specified IF Type Error";
          logger.error(errMsg);
          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendRequest(InterfaceChangeStateRequest request,
      InterfaceChangeStateRequestBody requestBody, FcNode fcNode) throws MsfException {
    try {
      logger.methodStart(new String[] { "request", "requestBody" }, new Object[] { request, requestBody });
      InterfaceChangeStateEcRequestBody ecRequestBody = new InterfaceChangeStateEcRequestBody();
      ecRequestBody.setStatus(requestBody.getBlockadeStatusEnum().getMessage());
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(JsonUtil.toJson(ecRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase responseBase = RestClient.sendRequest(
          EcRequestUri.CHANGE_IF_STATUS.getHttpMethod(), EcRequestUri.CHANGE_IF_STATUS
              .getUri(String.valueOf(fcNode.getEcNodeId()), request.getIfType(), request.getIfId()),
          requestBase, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(responseBase.getResponseBody())) {
        ErrorInternalResponseBody body = JsonUtil.fromJson(responseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = body.getErrorCode();
      }

      checkRestResponseHttpStatusCode(responseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase creatIfBlockageStatusUpdateAsyncData() {
    try {
      logger.methodStart();
      RestResponseBase responseBase = new RestResponseBase();
      responseBase.setHttpStatusCode(HttpStatus.OK_200);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }
}
