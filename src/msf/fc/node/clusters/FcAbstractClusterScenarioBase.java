
package msf.fc.node.clusters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.SwClusterData;
import msf.fc.common.config.type.system.SwCluster;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcNode;
import msf.fc.common.util.FcIpAddressUtil;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.L3ProtocolType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.VpnType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.AbstractClusterScenarioBase;
import msf.mfcfc.node.clusters.data.entity.SwClusterAddressDefinitionEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterEdgePointEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterRrEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterUniSupportProtocolEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of SW cluster-related
 * processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 *
 */
public abstract class FcAbstractClusterScenarioBase<T extends RestRequestBase> extends AbstractClusterScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractClusterScenarioBase.class);

  protected static final Integer MANAGEMENT_ADDRESS_PREFIX = 32;

  protected SwClusterForOwnerEntity getSwClusterForOwner(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      SwClusterForOwnerEntity swClusterForOwner = new SwClusterForOwnerEntity();

      SwClusterData dataConfSwClusterData = FcConfigManager.getInstance().getDataConfSwClusterData();

      swClusterForOwner.setClusterId(String.valueOf(dataConfSwClusterData.getSwCluster().getSwClusterId()));

      List<FcNode> leafNodes = getLeafNodes(sessionWrapper);
      if (CollectionUtils.isNotEmpty(leafNodes)) {

        NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();

        TreeMap<String, VpnType> nodeVpnMap = getNodeVpnMap(nodeReadListEcResponseBody);

        swClusterForOwner.setEdgePoints(getEdgePoint(nodeVpnMap, sessionWrapper));

        swClusterForOwner.setUniSupportProtocols(
            getUniSupportProtocol(nodeVpnMap.containsValue(VpnType.L2VPN), nodeVpnMap.containsValue(VpnType.L3VPN)));
      } else {

        SwClusterEdgePointEntity edgePoint = new SwClusterEdgePointEntity();
        edgePoint.setL2EdgePointList(new ArrayList<>());
        edgePoint.setL3EdgePointList(new ArrayList<>());
        swClusterForOwner.setEdgePoints(edgePoint);

        SwClusterUniSupportProtocolEntity uniSupportProtocol = new SwClusterUniSupportProtocolEntity();
        uniSupportProtocol.setL2(false);
        uniSupportProtocol.setL3(false);
        uniSupportProtocol.setL3ProtocolList(getL3Protocols(false));
        swClusterForOwner.setUniSupportProtocols(uniSupportProtocol);
      }

      SwCluster systemConfSwClusterData = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster();

      swClusterForOwner.setMaxLeafNum(dataConfSwClusterData.getSwCluster().getMaxLeafNum());
      swClusterForOwner.setMaxSpineNum(dataConfSwClusterData.getSwCluster().getMaxSpineNum());
      swClusterForOwner.setEcControlAddress(systemConfSwClusterData.getEcControlAddress());
      swClusterForOwner.setEcControlPort(systemConfSwClusterData.getEcControlPort());
      swClusterForOwner.setAsNumber(dataConfSwClusterData.getSwCluster().getAsNum());
      SwClusterAddressDefinitionEntity addressDefinition = new SwClusterAddressDefinitionEntity();
      addressDefinition.setInterfaceStartAddress(dataConfSwClusterData.getSwCluster().getInchannelStartAddress());

      addressDefinition.setLoopbackStartAddress(FcIpAddressUtil.getX0lloi(1));
      addressDefinition.setManagementStartAddress(dataConfSwClusterData.getSwCluster().getOutchannelStartAddress());

      addressDefinition.setManagementAddressPrefix(MANAGEMENT_ADDRESS_PREFIX);
      swClusterForOwner.setAddressDefinitions(addressDefinition);

      SwClusterRrEntity rrs = new SwClusterRrEntity();
      if (CollectionUtils.isNotEmpty(dataConfSwClusterData.getRrs().getLeafRr())) {

        int leafRrSwClusterId = dataConfSwClusterData.getRrs().getLeafRr().get(0).getLeafRrSwClusterId();
        String swClusterId = String.valueOf(leafRrSwClusterId);
        rrs.setPeerCluster(swClusterId);
        if (leafRrSwClusterId == dataConfSwClusterData.getSwCluster().getSwClusterId()) {

          rrs.setAccommodatedClusterList(Arrays.asList(swClusterId));
        } else {
          rrs.setAccommodatedClusterList(new ArrayList<>());
        }
      } else {

        rrs.setPeerCluster(null);
        rrs.setAccommodatedClusterList(new ArrayList<>());
      }
      swClusterForOwner.setRrs(rrs);
      return swClusterForOwner;
    } finally {
      logger.methodEnd();
    }
  }

  protected SwClusterForUserEntity getSwClusterForUser(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      SwClusterForUserEntity swClusterForUser = new SwClusterForUserEntity();

      SwClusterData dataConfSwClusterData = FcConfigManager.getInstance().getDataConfSwClusterData();

      swClusterForUser.setClusterId(String.valueOf(dataConfSwClusterData.getSwCluster().getSwClusterId()));

      List<FcNode> leafNodes = getLeafNodes(sessionWrapper);
      if (CollectionUtils.isNotEmpty(leafNodes)) {

        NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();

        TreeMap<String, VpnType> nodeVpnMap = getNodeVpnMap(nodeReadListEcResponseBody);

        swClusterForUser.setEdgePoints(getEdgePoint(nodeVpnMap, sessionWrapper));

        swClusterForUser.setUniSupportProtocols(
            getUniSupportProtocol(nodeVpnMap.containsValue(VpnType.L2VPN), nodeVpnMap.containsValue(VpnType.L3VPN)));
      } else {

        SwClusterEdgePointEntity edgePoint = new SwClusterEdgePointEntity();
        edgePoint.setL2EdgePointList(new ArrayList<>());
        edgePoint.setL3EdgePointList(new ArrayList<>());
        swClusterForUser.setEdgePoints(edgePoint);

        SwClusterUniSupportProtocolEntity uniSupportProtocol = new SwClusterUniSupportProtocolEntity();
        uniSupportProtocol.setL2(false);
        uniSupportProtocol.setL3(false);
        uniSupportProtocol.setL3ProtocolList(getL3Protocols(false));
        swClusterForUser.setUniSupportProtocols(uniSupportProtocol);
      }
      return swClusterForUser;
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<String, VpnType> getNodeVpnMap(NodeReadListEcResponseBody nodeReadListEcResponseBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeReadListEcResponseBody" }, new Object[] { nodeReadListEcResponseBody });
      TreeMap<String, VpnType> nodeVpnMap = new TreeMap<>();
      for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
        if (nodeEcEntity.getVpnType() != null) {
          VpnType vpnType = VpnType.getEnumFromMessage(nodeEcEntity.getVpnType());
          if (vpnType != null) {
            nodeVpnMap.put(nodeEcEntity.getNodeId(), vpnType);
          } else {

            throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                "illegal parameter. vpnType = " + nodeEcEntity.getVpnType());
          }
        }
      }
      return nodeVpnMap;
    } finally {
      logger.methodEnd();
    }
  }

  private SwClusterEdgePointEntity getEdgePoint(TreeMap<String, VpnType> nodeVpnMap, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeVpnMap" }, new Object[] { nodeVpnMap });
      SwClusterEdgePointEntity edgePointData = new SwClusterEdgePointEntity();
      ArrayList<String> l2EdgePointList = new ArrayList<>();
      ArrayList<String> l3EdgePointList = new ArrayList<>();
      FcEdgePointDao fcEdgePointDao = new FcEdgePointDao();
      List<FcEdgePoint> fcEdgePoints = fcEdgePointDao.readList(sessionWrapper);

      for (FcEdgePoint fcEdgePoint : fcEdgePoints) {

        FcNode node = null;
        if (fcEdgePoint.getPhysicalIf() != null) {
          node = fcEdgePoint.getPhysicalIf().getNode();
        } else if (fcEdgePoint.getBreakoutIf() != null) {
          node = fcEdgePoint.getBreakoutIf().getNode();
        } else {
          node = fcEdgePoint.getLagIf().getNode();
        }

        VpnType vpnType = nodeVpnMap.get(String.valueOf(node.getEcNodeId()));
        if (vpnType.equals(VpnType.L2VPN)) {
          l2EdgePointList.add(String.valueOf(fcEdgePoint.getEdgePointId()));
        } else if (vpnType.equals(VpnType.L3VPN)) {
          l3EdgePointList.add(String.valueOf(fcEdgePoint.getEdgePointId()));
        }
      }
      edgePointData.setL2EdgePointList(l2EdgePointList);
      edgePointData.setL3EdgePointList(l3EdgePointList);
      return edgePointData;
    } finally {
      logger.methodEnd();
    }
  }

  private SwClusterUniSupportProtocolEntity getUniSupportProtocol(boolean isL2Vpn, boolean isL3Vpn) {
    try {
      logger.methodStart(new String[] { "isL2Vpn", "isL3Vpn" }, new Object[] { isL2Vpn, isL3Vpn });
      SwClusterUniSupportProtocolEntity uniSupportProtocol = new SwClusterUniSupportProtocolEntity();
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

  protected NodeReadListEcResponseBody sendNodeReadList() throws MsfException {
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

  private List<FcNode> getLeafNodes(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      FcNodeDao nodeDao = new FcNodeDao();

      List<FcNode> leafNodes = nodeDao.readList(sessionWrapper, NodeType.LEAF.getCode());
      return leafNodes;
    } finally {
      logger.methodEnd();
    }
  }

}
