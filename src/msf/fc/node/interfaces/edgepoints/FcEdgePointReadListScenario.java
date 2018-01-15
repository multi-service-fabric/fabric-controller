
package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadDetailListOwnerResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadDetailListUserResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadListResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForOwnerEntity;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForUserEntity;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for Edge-Point interface information list acquisition.
 *
 * @author NTT
 *
 */
public class FcEdgePointReadListScenario extends FcAbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEdgePointReadListScenario.class);

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
  public FcEdgePointReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getClusterId());

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

      if (request.getUserType() != null) {

        if (!RestFormatOption.DETAIL_LIST.equals(request.getFormatEnum())) {
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
              "To set the \"userType\" must be set to \"format\" = detail-list. ");
        }

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

        List<FcEdgePoint> fcEdgePoints = fcEdgePointDao.readList(sessionWrapper);

        responseBase = responseEdgePointReadListData(fcEdgePoints, request.getFormat(), request.getUserType(),
            sessionWrapper);

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

  private RestResponseBase responseEdgePointReadListData(List<FcEdgePoint> fcEdgePoints, String format, String userType,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (!fcEdgePoints.isEmpty()) {

          NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();
          if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {

            EdgePointReadDetailListOwnerResponseBody body = new EdgePointReadDetailListOwnerResponseBody();
            body.setEdgePointList(
                getEdgePoinForOwnertEntities(fcEdgePoints, sessionWrapper, nodeReadListEcResponseBody.getNodeList()));
            return createRestResponse(body, HttpStatus.OK_200);
          } else {

            EdgePointReadDetailListUserResponseBody body = new EdgePointReadDetailListUserResponseBody();
            body.setEdgePointList(
                getEdgePointForUserEntities(fcEdgePoints, sessionWrapper, nodeReadListEcResponseBody.getNodeList()));
            return createRestResponse(body, HttpStatus.OK_200);
          }
        } else {

          if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {

            EdgePointReadDetailListOwnerResponseBody body = new EdgePointReadDetailListOwnerResponseBody();
            body.setEdgePointList(new ArrayList<>());
            return createRestResponse(body, HttpStatus.OK_200);
          } else {

            EdgePointReadDetailListUserResponseBody body = new EdgePointReadDetailListUserResponseBody();
            body.setEdgePointList(new ArrayList<>());
            return createRestResponse(body, HttpStatus.OK_200);
          }
        }
      } else {
        EdgePointReadListResponseBody body = new EdgePointReadListResponseBody();
        body.setEdgePointIdList(getEdgePointIdList(fcEdgePoints));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private NodeReadListEcResponseBody sendNodeReadList() throws MsfException {
    try {
      logger.methodStart();
      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ_LIST.getHttpMethod(),
          EcRequestUri.NODE_READ_LIST.getUri(), null, ecControlIpAddress, ecControlPort);

      NodeReadListEcResponseBody nodeReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          NodeReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          nodeReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return nodeReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private List<EdgePointForOwnerEntity> getEdgePoinForOwnertEntities(List<FcEdgePoint> fcEdgePoints,
      SessionWrapper sessionWrapper, List<NodeEcEntity> nodeList) throws MsfException {
    try {
      logger.methodStart();
      ArrayList<EdgePointForOwnerEntity> edgePoinForOwnertEntities = new ArrayList<>();
      boolean isExist;
      for (FcEdgePoint fcEdgePoint : fcEdgePoints) {
        isExist = false;
        for (NodeEcEntity nodeEcEntity : nodeList) {
          String ecNodeId = null;

          if (fcEdgePoint.getPhysicalIf() != null) {
            ecNodeId = String.valueOf(fcEdgePoint.getPhysicalIf().getNode().getEcNodeId());
          } else if (fcEdgePoint.getLagIf() != null) {
            ecNodeId = String.valueOf(fcEdgePoint.getLagIf().getNode().getEcNodeId());
          } else {
            ecNodeId = String.valueOf(fcEdgePoint.getBreakoutIf().getNode().getEcNodeId());
          }

          if (ecNodeId.equals(nodeEcEntity.getNodeId())) {
            edgePoinForOwnertEntities.add(getEdgePointForOwner(fcEdgePoint, sessionWrapper, nodeEcEntity));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      return edgePoinForOwnertEntities;
    } finally {
      logger.methodEnd();
    }
  }

  private List<EdgePointForUserEntity> getEdgePointForUserEntities(List<FcEdgePoint> fcEdgePoints,
      SessionWrapper sessionWrapper, List<NodeEcEntity> nodeList) throws MsfException {
    try {
      logger.methodStart();
      ArrayList<EdgePointForUserEntity> edgePointForUserEntities = new ArrayList<>();
      boolean isExist;
      for (FcEdgePoint fcEdgePoint : fcEdgePoints) {
        isExist = false;
        for (NodeEcEntity nodeEcEntity : nodeList) {
          String ecNodeId = null;

          if (fcEdgePoint.getPhysicalIf() != null) {
            ecNodeId = String.valueOf(fcEdgePoint.getPhysicalIf().getNode().getEcNodeId());
          } else if (fcEdgePoint.getLagIf() != null) {
            ecNodeId = String.valueOf(fcEdgePoint.getLagIf().getNode().getEcNodeId());
          } else {
            ecNodeId = String.valueOf(fcEdgePoint.getBreakoutIf().getNode().getEcNodeId());
          }

          if (ecNodeId.equals(nodeEcEntity.getNodeId())) {
            edgePointForUserEntities.add(getEdgePointForUser(fcEdgePoint, sessionWrapper, nodeEcEntity));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      return edgePointForUserEntities;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getEdgePointIdList(List<FcEdgePoint> fcEdgePoints) {
    try {
      logger.methodStart();
      List<String> edgePointIdList = new ArrayList<>();
      for (FcEdgePoint fcEdgePoint : fcEdgePoints) {
        edgePointIdList.add(String.valueOf(fcEdgePoint.getEdgePointId()));
      }
      return edgePointIdList;
    } finally {
      logger.methodEnd();
    }
  }
}
