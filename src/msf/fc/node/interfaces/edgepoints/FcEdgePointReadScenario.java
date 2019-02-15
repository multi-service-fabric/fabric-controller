
package msf.fc.node.interfaces.edgepoints;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.rest.ec.node.interfaces.breakout.data.BreakoutIfReadEcResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfReadEcResponseBody;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfReadEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadEcResponseBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadOwnerResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadUserResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for the Edge-Point interface information acquisition.
 *
 * @author NTT
 *
 */
public class FcEdgePointReadScenario extends FcAbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEdgePointReadScenario.class);

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
  public FcEdgePointReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getEdgePointId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);
      if (request.getUserType() != null) {
        ParameterCheckUtil.checkNotNull(request.getUserTypeEnum());
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
        FcEdgePointDao fcEdgePointDao = new FcEdgePointDao();

        FcEdgePoint fcEdgePoint = getEdgePoint(sessionWrapper, fcEdgePointDao,
            Integer.parseInt(request.getEdgePointId()));

        responseBase = responseEdgePointReadData(fcEdgePoint, request.getUserType(), sessionWrapper);

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

  private RestResponseBase responseEdgePointReadData(FcEdgePoint fcEdgePoint, String userType,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      NodeReadEcResponseBody nodeReadEcResponseBody = sendNodeRead(fcEdgePoint);
      Object interfaceQosResponseBody = sendInterfaceReadToQos(fcEdgePoint);
      if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
        EdgePointReadOwnerResponseBody body = new EdgePointReadOwnerResponseBody();
        body.setEdgePoint(getEdgePointForOwner(fcEdgePoint, sessionWrapper, nodeReadEcResponseBody.getNode(),
            interfaceQosResponseBody));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        EdgePointReadUserResponseBody body = new EdgePointReadUserResponseBody();
        body.setEdgePoint(getEdgePointForUser(fcEdgePoint, sessionWrapper, nodeReadEcResponseBody.getNode(),
            interfaceQosResponseBody));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private NodeReadEcResponseBody sendNodeRead(FcEdgePoint fcEdgePoint) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      String ecNodeId = null;

      if (fcEdgePoint.getPhysicalIf() != null) {
        ecNodeId = String.valueOf(fcEdgePoint.getPhysicalIf().getNode().getEcNodeId());
      } else if (fcEdgePoint.getLagIf() != null) {
        ecNodeId = String.valueOf(fcEdgePoint.getLagIf().getNode().getEcNodeId());
      } else {
        ecNodeId = String.valueOf(fcEdgePoint.getBreakoutIf().getNode().getEcNodeId());
      }

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ.getHttpMethod(),
          EcRequestUri.NODE_READ.getUri(ecNodeId), null, ecControlIpAddress, ecControlPort);

      NodeReadEcResponseBody nodeReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          NodeReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          nodeReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return nodeReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private Object sendInterfaceReadToQos(FcEdgePoint fcEdgePoint) throws MsfException {
    try {
      logger.methodStart();

      if (fcEdgePoint.getPhysicalIf() != null) {
        FcPhysicalIf fcPhysicalIf = fcEdgePoint.getPhysicalIf();
        PhysicalIfReadEcResponseBody sendPhysicalInterfaceRead = sendPhysicalInterfaceRead(fcPhysicalIf.getNode(),
            fcPhysicalIf.getPhysicalIfId());
        return sendPhysicalInterfaceRead.getPhysicalIf().getQos();
      } else if (fcEdgePoint.getLagIf() != null) {
        FcLagIf fcLagIf = fcEdgePoint.getLagIf();
        LagIfReadEcResponseBody sendLagInterfaceRead = sendLagInterfaceRead(fcLagIf.getNode(),
            String.valueOf(fcLagIf.getLagIfId()));
        return sendLagInterfaceRead.getLagIf().getQos();
      } else {
        FcBreakoutIf fcBreakoutIf = fcEdgePoint.getBreakoutIf();
        BreakoutIfReadEcResponseBody sendBreakoutInterfaceRead = sendBreakoutInterfaceRead(fcBreakoutIf.getNode(),
            fcBreakoutIf.getBreakoutIfId());
        return sendBreakoutInterfaceRead.getBreakoutIf().getQos();
      }
    } finally {
      logger.methodEnd();
    }
  }
}
