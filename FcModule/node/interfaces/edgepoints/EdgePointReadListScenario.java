package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.L3ProtocolType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.RestUserTypeOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.constant.VpnType;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.Node;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.node.interfaces.edgepoints.data.EdgePointReadDetailListOwnerResponseBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointReadDetailListUserResponseBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointReadListResponseBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.fc.node.interfaces.edgepoints.data.entity.L2EdgePointForOwnerEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.L2EdgePointForUserEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.L3EdgePointForOwnerEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.L3EdgePointForUserEntity;

public class EdgePointReadListScenario extends AbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointReadListScenario.class);

  public EdgePointReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {

        ParameterCheckUtil.checkNotNull(request.getFormatEnum());

      }

      if (request.getUserType() != null) {
        if (request.getFormat() == null
            || !(RestFormatOption.DETAIL_LIST.equals(RestFormatOption.getEnumFromMessage(request.getFormat())))) {
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
        EdgePointDao edgePointDao = new EdgePointDao();
        List<EdgePoint> edgePoints = edgePointDao.readList(sessionWrapper, request.getClusterId());

        if (edgePoints.isEmpty()) {
          checkSwCluster(sessionWrapper, request.getClusterId());
        }

        responseBase = responseEdgePointReadListData(edgePoints, request.getFormat(), request.getUserType(),
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

  private RestResponseBase responseEdgePointReadListData(List<EdgePoint> edgePoints, String format, String userType,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
          EdgePointReadDetailListOwnerResponseBody body = getEdgePointReadDetailListOwnerResponseBody(edgePoints,
              sessionWrapper);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          EdgePointReadDetailListUserResponseBody body = getEdgePointReadDetailListUserResponseBody(edgePoints,
              sessionWrapper);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {
        EdgePointReadListResponseBody body = new EdgePointReadListResponseBody();
        List<String> l2EdgePointIdList = new ArrayList<>();
        List<String> l3EdgePointIdList = new ArrayList<>();
        NodeDao nodeDao = new NodeDao();
        for (EdgePoint edgePoint : edgePoints) {
          Node node = nodeDao.read(sessionWrapper, edgePoint.getSwCluster().getSwClusterId(),
              edgePoint.getId().getEdgePointId());
          if (node.getVpnTypeEnum().equals(VpnType.L2VPN)) {
            l2EdgePointIdList.add(String.valueOf(edgePoint.getId().getEdgePointId()));
          } else if (node.getVpnTypeEnum().equals(VpnType.L3VPN)) {
            l3EdgePointIdList.add(String.valueOf(edgePoint.getId().getEdgePointId()));
          }
        }
        body.setL2EdgePointIdList(l2EdgePointIdList);
        body.setL3EdgePointIdList(l3EdgePointIdList);
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointReadDetailListOwnerResponseBody getEdgePointReadDetailListOwnerResponseBody(
      List<EdgePoint> edgePoints, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      NodeDao nodeDao = new NodeDao();
      EdgePointReadDetailListOwnerResponseBody body = new EdgePointReadDetailListOwnerResponseBody();
      List<L2EdgePointForOwnerEntity> l2EdgePointList = new ArrayList<>();
      List<L3EdgePointForOwnerEntity> l3EdgePointList = new ArrayList<>();
      for (EdgePoint edgePoint : edgePoints) {
        L2EdgePointForOwnerEntity l2EdgePointForOwner = new L2EdgePointForOwnerEntity();
        Node node = nodeDao.read(sessionWrapper, edgePoint.getSwCluster().getSwClusterId(),
            edgePoint.getId().getEdgePointId());
        if (node.getVpnTypeEnum().equals(VpnType.L2VPN)) {
          l2EdgePointForOwner.setEdgePointId(String.valueOf(edgePoint.getId().getEdgePointId()));
          l2EdgePointForOwner.setBaseIf(getBaseIf(edgePoint, sessionWrapper));
          l2EdgePointList.add(l2EdgePointForOwner);
        } else if (node.getVpnTypeEnum().equals(VpnType.L3VPN)) {
          L3EdgePointForOwnerEntity l3EdgePointForOwner = new L3EdgePointForOwnerEntity();
          l3EdgePointForOwner.setEdgePointId(String.valueOf(edgePoint.getId().getEdgePointId()));
          l3EdgePointForOwner.setBaseIf(getBaseIf(edgePoint, sessionWrapper));
          l3EdgePointForOwner.setSupportProtocolList(getL3Protocols(true));
          l3EdgePointList.add(l3EdgePointForOwner);
        }
      }
      body.setL2EdgePointList(l2EdgePointList);
      body.setL3EdgePointList(l3EdgePointList);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointReadDetailListUserResponseBody getEdgePointReadDetailListUserResponseBody(List<EdgePoint> edgePoints,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      NodeDao nodeDao = new NodeDao();
      EdgePointReadDetailListUserResponseBody body = new EdgePointReadDetailListUserResponseBody();
      List<L2EdgePointForUserEntity> l2EdgePointList = new ArrayList<>();
      List<L3EdgePointForUserEntity> l3EdgePointList = new ArrayList<>();
      for (EdgePoint edgePoint : edgePoints) {
        Node node = nodeDao.read(sessionWrapper, edgePoint.getSwCluster().getSwClusterId(),
            edgePoint.getId().getEdgePointId());
        if (node.getVpnTypeEnum().equals(VpnType.L2VPN)) {
          L2EdgePointForUserEntity l2EdgePointForUser = new L2EdgePointForUserEntity();
          l2EdgePointForUser.setEdgePointId(String.valueOf(edgePoint.getId().getEdgePointId()));
          l2EdgePointList.add(l2EdgePointForUser);
        } else if (node.getVpnTypeEnum().equals(VpnType.L3VPN)) {
          L3EdgePointForUserEntity l3EdgePointForUser = new L3EdgePointForUserEntity();
          l3EdgePointForUser.setEdgePointId(String.valueOf(edgePoint.getId().getEdgePointId()));
          l3EdgePointForUser.setSupportProtocolList(getL3Protocols(true));
          l3EdgePointList.add(l3EdgePointForUser);
        }
      }
      body.setL2EdgePointList(l2EdgePointList);
      body.setL3EdgePointList(l3EdgePointList);
      return body;
    } finally {
      logger.methodEnd();
    }

  }

  private ArrayList<String> getL3Protocols(boolean isL3Vpn) {
    try {
      logger.methodStart(new String[] { "isL3Vpn" }, new Object[] { isL3Vpn });
      ArrayList<String> l3Protocols = new ArrayList<>();
      if (isL3Vpn) {
        l3Protocols.add(L3ProtocolType.BGP.getMessage());
        l3Protocols.add(L3ProtocolType.OSPF.getMessage());
        l3Protocols.add(L3ProtocolType.STATIC.getMessage());
        l3Protocols.add(L3ProtocolType.VRRP.getMessage());
      }
      return l3Protocols;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkSwCluster(SessionWrapper sessionWrapper, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterId" },
          new Object[] { sessionWrapper, swClusterId });
      SwClusterDao swClusterDao = new SwClusterDao();
      SwCluster swCluster = swClusterDao.read(sessionWrapper, swClusterId);
      if (swCluster == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
    } finally {
      logger.methodEnd();
    }
  }
}
