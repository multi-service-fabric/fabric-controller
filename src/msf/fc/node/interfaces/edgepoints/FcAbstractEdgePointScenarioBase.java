
package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.L3ProtocolType;
import msf.mfcfc.common.constant.VpnType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointBaseIfEntity;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForOwnerEntity;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForUserEntity;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointSupportProtocolForOwnerEntity;
import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointSupportProtocolForUserEntity;

/**
 * Abstract class to implement common process of Edge-Point interface-related
 * processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractEdgePointScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractEdgePointScenarioBase.class);

  protected FcEdgePoint getEdgePoint(SessionWrapper sessionWrapper, FcEdgePointDao fcEdgePointDao, Integer edgePointId)
      throws MsfException {
    try {
      logger.methodStart();

      FcEdgePoint fcEdgePoint = fcEdgePointDao.read(sessionWrapper, edgePointId);
      if (fcEdgePoint == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = edgePoint");
      }
      return fcEdgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  protected EdgePointBaseIfEntity getBaseIf(FcEdgePoint fcEdgePoint, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint", "sessionWrapper" }, new Object[] { fcEdgePoint, sessionWrapper });
      EdgePointBaseIfEntity baseIf = new EdgePointBaseIfEntity();

      String leafNodeId = null;
      FcPhysicalIf fcPhysicalIf = fcEdgePoint.getPhysicalIf();
      FcLagIf fcLagIf = fcEdgePoint.getLagIf();
      FcBreakoutIf fcBreakoutIf = fcEdgePoint.getBreakoutIf();

      if (fcPhysicalIf != null) {
        baseIf.setPhysicalIfId(String.valueOf(fcPhysicalIf.getPhysicalIfId()));
        leafNodeId = String.valueOf(fcPhysicalIf.getNode().getNodeId());
      } else if (fcLagIf != null) {
        baseIf.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));
        leafNodeId = String.valueOf(fcLagIf.getNode().getNodeId());
      } else {
        baseIf.setBreakoutIfId(String.valueOf(fcBreakoutIf.getBreakoutIfId()));
        leafNodeId = String.valueOf(fcBreakoutIf.getNode().getNodeId());
      }
      baseIf.setLeafNodeId(leafNodeId);

      return baseIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected EdgePointForOwnerEntity getEdgePointForOwner(FcEdgePoint fcEdgePoint, SessionWrapper sessionWrapper,
      NodeEcEntity nodeEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcEdgePoint", "sessionWrapper" },
          new Object[] { fcEdgePoint, sessionWrapper });
      EdgePointForOwnerEntity edgePointForOwner = new EdgePointForOwnerEntity();
      edgePointForOwner.setEdgePointId(String.valueOf(fcEdgePoint.getEdgePointId()));
      edgePointForOwner.setSupportProtocols(getSupportProtocolForOwner(fcEdgePoint, sessionWrapper, nodeEcEntity));
      edgePointForOwner.setBaseIf(getBaseIf(fcEdgePoint, sessionWrapper));
      edgePointForOwner.setTrafficThreshold(fcEdgePoint.getTrafficThreshold());
      return edgePointForOwner;
    } finally {
      logger.methodEnd();
    }
  }

  protected EdgePointForUserEntity getEdgePointForUser(FcEdgePoint fcEdgePoint, SessionWrapper sessionWrapper,
      NodeEcEntity nodeEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint" }, new Object[] { fcEdgePoint });
      EdgePointForUserEntity edgePointForUser = new EdgePointForUserEntity();
      edgePointForUser.setEdgePointId(String.valueOf(fcEdgePoint.getEdgePointId()));
      edgePointForUser.setSupportProtocols(getSupportProtocolForUser(fcEdgePoint, sessionWrapper, nodeEcEntity));
      return edgePointForUser;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointSupportProtocolForOwnerEntity getSupportProtocolForOwner(FcEdgePoint fcEdgePoint,
      SessionWrapper sessionWrapper, NodeEcEntity nodeEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcEdgePoint" }, new Object[] { fcEdgePoint });
      EdgePointSupportProtocolForOwnerEntity supportProtocol = new EdgePointSupportProtocolForOwnerEntity();
      boolean isL2Vpn = false;
      boolean isL3Vpn = false;
      if (nodeEcEntity.getVpnType() != null) {
        if (nodeEcEntity.getVpnType().equals(VpnType.L2VPN.getMessage())) {
          isL2Vpn = true;
        } else {
          isL3Vpn = true;
        }
      }
      supportProtocol.setL2(isL2Vpn);
      supportProtocol.setL3(isL3Vpn);
      supportProtocol.setL3ProtocolList(getL3Protocols(isL3Vpn));
      return supportProtocol;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointSupportProtocolForUserEntity getSupportProtocolForUser(FcEdgePoint fcEdgePoint,
      SessionWrapper sessionWrapper, NodeEcEntity nodeEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcEdgePoint" }, new Object[] { fcEdgePoint });
      EdgePointSupportProtocolForUserEntity supportProtocol = new EdgePointSupportProtocolForUserEntity();
      boolean isL2Vpn = false;
      boolean isL3Vpn = false;
      if (nodeEcEntity.getVpnType() != null) {
        if (nodeEcEntity.getVpnType().equals(VpnType.L2VPN.getMessage())) {
          isL2Vpn = true;
        } else {
          isL3Vpn = true;
        }
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
        l3Protocols.add(L3ProtocolType.STATIC.getMessage());
        l3Protocols.add(L3ProtocolType.VRRP.getMessage());
      }
      return l3Protocols;
    } finally {
      logger.methodEnd();
    }
  }
}
