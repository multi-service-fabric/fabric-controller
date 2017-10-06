package msf.fc.node.clusters;

import java.util.ArrayList;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.L3ProtocolType;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.VpnType;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.Node;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.clusters.data.entity.AddressDefinitionEntity;
import msf.fc.node.clusters.data.entity.EdgePointEntity;
import msf.fc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.fc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.fc.node.clusters.data.entity.UniSupportProtocolEntity;
import msf.fc.rest.common.AbstractResponseBody;

public abstract class AbstractClusterScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractClusterScenarioBase.class);

  protected SwClusterForOwnerEntity getSwClusterForOwner(SwCluster swCluster, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      SwClusterForOwnerEntity swClusterForOwner = new SwClusterForOwnerEntity();
      swClusterForOwner.setSwClusterId(swCluster.getSwClusterId());
      swClusterForOwner.setEdgePoint(getEdgePoint(swCluster, sessionWrapper));
      swClusterForOwner.setUniSupportProtocol(getUniSupportProtocol(swCluster));
      swClusterForOwner.setMaxLeafNum(swCluster.getMaxLeafNum());
      swClusterForOwner.setMaxSpineNum(swCluster.getMaxSpineNum());
      swClusterForOwner.setEcControlAddress(swCluster.getEcControlAddress());
      swClusterForOwner.setEcControlPort(swCluster.getEcControlPort());
      swClusterForOwner.setAsNum(swCluster.getAsNum());
      AddressDefinitionEntity addressDefinition = new AddressDefinitionEntity();
      addressDefinition.setInterfaceStartAddress(swCluster.getInterfaceStartAddress());
      addressDefinition.setLoopbackStartAddress(swCluster.getLoopbackStartAddress());
      addressDefinition.setManagementStartAddress(swCluster.getManagementStartAddress());
      addressDefinition.setManagementAddressPrefix(swCluster.getManagementAddressPrefix());
      swClusterForOwner.setAddressDefinition(addressDefinition);
      return swClusterForOwner;
    } finally {
      logger.methodEnd();
    }
  }

  protected SwClusterForUserEntity getSwClusterForUser(SwCluster swCluster, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      SwClusterForUserEntity swClusterForUser = new SwClusterForUserEntity();
      swClusterForUser.setSwClusterId(swCluster.getSwClusterId());
      swClusterForUser.setEdgePoint(getEdgePoint(swCluster, sessionWrapper));
      swClusterForUser.setUniSupportProtocol(getUniSupportProtocol(swCluster));
      return swClusterForUser;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePointEntity getEdgePoint(SwCluster swCluster, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      EdgePointEntity edgePointData = new EdgePointEntity();
      ArrayList<String> l2EdgePointList = new ArrayList<>();
      ArrayList<String> l3EdgePointList = new ArrayList<>();
      NodeDao nodeDao = new NodeDao();
      for (EdgePoint edgePoint : swCluster.getEdgePoints()) {
        Node node = nodeDao.read(sessionWrapper, edgePoint.getSwCluster().getSwClusterId(),
            edgePoint.getId().getEdgePointId());
        if (node.getVpnTypeEnum().equals(VpnType.L2VPN)) {
          l2EdgePointList.add(String.valueOf(edgePoint.getId().getEdgePointId()));
        } else if (node.getVpnTypeEnum().equals(VpnType.L3VPN)) {
          l3EdgePointList.add(String.valueOf(edgePoint.getId().getEdgePointId()));
        }
      }
      edgePointData.setL2EdgePointList(l2EdgePointList);
      edgePointData.setL3EdgePointList(l3EdgePointList);
      return edgePointData;
    } finally {
      logger.methodEnd();
    }
  }

  private UniSupportProtocolEntity getUniSupportProtocol(SwCluster swCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      UniSupportProtocolEntity uniSupportProtocol = new UniSupportProtocolEntity();
      boolean isL2Vpn = false;
      boolean isL3Vpn = false;
      for (Equipment equipment : swCluster.getEquipments()) {
        for (Node node : equipment.getNodes()) {

          if (node.getNodeTypeEnum().equals(NodeType.LEAF)) {
            if (node.getVpnTypeEnum().equals(VpnType.L2VPN)) {
              isL2Vpn = true;
            } else if (node.getVpnTypeEnum().equals(VpnType.L3VPN)) {
              isL3Vpn = true;
            }
          }
          if ((isL2Vpn) && (isL3Vpn)) {
            break;
          }
        }
        if ((isL2Vpn) && (isL3Vpn)) {
          break;
        }
      }
      uniSupportProtocol.setL2(isL2Vpn);
      uniSupportProtocol.setL3(isL3Vpn);
      uniSupportProtocol.setL3ProtocolList(getL3Protocols(isL3Vpn));
      return uniSupportProtocol;
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

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

}
