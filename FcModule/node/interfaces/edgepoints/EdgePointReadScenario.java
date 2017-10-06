package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.L3ProtocolType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestUserTypeOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.constant.VpnType;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.interfaces.edgepoints.data.EdgePointReadOwnerResponseBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointReadUserResponseBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.fc.node.interfaces.edgepoints.data.entity.EdgePointForOwnerEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.EdgePointForUserEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.SupportProtocolEntity;

public class EdgePointReadScenario extends AbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointReadScenario.class);

  public EdgePointReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

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
        EdgePointDao edgePointDao = new EdgePointDao();
        EdgePoint edgePoint = getEdgePoint(sessionWrapper, edgePointDao, request.getClusterId(),
            Integer.parseInt(request.getEdgePointId()));

        responseBase = responseEdgePointReadData(edgePoint, request.getUserType(), sessionWrapper);

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

  private RestResponseBase responseEdgePointReadData(EdgePoint edgePoint, String userType,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
        EdgePointReadOwnerResponseBody body = new EdgePointReadOwnerResponseBody();
        body.setEdgePoint(getEdgePointForOwner(edgePoint, sessionWrapper));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        EdgePointReadUserResponseBody body = new EdgePointReadUserResponseBody();
        body.setEdgePoint(getEdgePointForUser(edgePoint, sessionWrapper));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointForOwnerEntity getEdgePointForOwner(EdgePoint edgePoint, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint", "sessionWrapper" }, new Object[] { edgePoint, sessionWrapper });
      EdgePointForOwnerEntity edgePointForOwner = new EdgePointForOwnerEntity();
      edgePointForOwner.setEdgePointId(String.valueOf(edgePoint.getId().getEdgePointId()));
      edgePointForOwner.setSupportProtocol(getSupportProtocol(edgePoint, sessionWrapper));
      edgePointForOwner.setBaseIf(getBaseIf(edgePoint, sessionWrapper));
      return edgePointForOwner;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointForUserEntity getEdgePointForUser(EdgePoint edgePoint, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint" }, new Object[] { edgePoint });
      EdgePointForUserEntity edgePointForUser = new EdgePointForUserEntity();
      edgePointForUser.setEdgePointId(String.valueOf(edgePoint.getId().getEdgePointId()));
      edgePointForUser.setSupportProtocol(getSupportProtocol(edgePoint, sessionWrapper));
      return edgePointForUser;
    } finally {
      logger.methodEnd();
    }
  }

  private SupportProtocolEntity getSupportProtocol(EdgePoint edgePoint, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint" }, new Object[] { edgePoint });
      SupportProtocolEntity supportProtocol = new SupportProtocolEntity();
      boolean isL2Vpn = false;
      boolean isL3Vpn = false;
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(sessionWrapper, edgePoint.getSwCluster().getSwClusterId(),
          edgePoint.getId().getEdgePointId());
      if (node.getVpnTypeEnum().equals(VpnType.L2VPN)) {
        isL2Vpn = true;
      } else if (node.getVpnTypeEnum().equals(VpnType.L3VPN)) {
        isL3Vpn = true;
      }
      supportProtocol.setL2(isL2Vpn);
      supportProtocol.setL3(isL3Vpn);
      supportProtocol.setL3ProtocolList(getL3Protocols(isL3Vpn));
      return supportProtocol;
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

}
