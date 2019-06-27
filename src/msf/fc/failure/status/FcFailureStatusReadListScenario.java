
package msf.fc.failure.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.data.entity.InterfacesEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.InterfaceOperationStatus;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusIfEntity;
import msf.mfcfc.failure.status.data.FailureStatusReadListResponseBody;
import msf.mfcfc.failure.status.data.FailureStatusRequest;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusPhysicalUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;

/**
 * Implementation class for the failure information list acquisition.
 *
 * @author NTT
 *
 */
public class FcFailureStatusReadListScenario extends FcAbstractFailureStatusScenarioBase<FailureStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcFailureStatusReadListScenario.class);

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
  public FcFailureStatusReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(FailureStatusRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    try {
      logger.methodStart();

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();

        int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();

        Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap = new HashMap<>();
        ifInfoEcMap.put(InterfaceType.PHYSICAL_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.BREAKOUT_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.LAG_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.VLAN_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());

        Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcAllMap = new HashMap<>();
        ifInfoEcAllMap.put(InterfaceType.PHYSICAL_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcAllMap.put(InterfaceType.BREAKOUT_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcAllMap.put(InterfaceType.LAG_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcAllMap.put(InterfaceType.VLAN_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());

        NodeReadListEcResponseBody ecListResponse = sendNodeReadList();

        FcNodeDao fcNodeDao = new FcNodeDao();
        List<FailureStatusIfFailureEntity> ifs = new ArrayList<>();

        Map<String, FcNode> fcNodeMap = new HashMap<>();

        for (NodeEcEntity node : ecListResponse.getNodeList()) {

          String ecNodeId = node.getNodeId();
          InterfaceReadListEcResponseBody ifListResponse = sendIfReadList(ecNodeId);

          VlanIfReadListEcResponseBody vlanIfListResponse = sendVlanIfReadList(ecNodeId);

          if (!fcNodeMap.containsKey(ecNodeId)) {
            FcNode fcNode = fcNodeDao.readByEcNodeId(session, Integer.valueOf(ecNodeId));
            fcNodeMap.put(ecNodeId, fcNode);
          }

          FcNode fcNode = fcNodeMap.get(ecNodeId);
          if (fcNode == null) {
            logger.info(
                "Failure status create skipped(Physical Unit :Ifs)."
                    + " Target Node not found. NodeEcEntity={0}, InterfacesEcEntityList={1}, VlanIfEcEntityList={2}.",
                node, ifListResponse.getIfs(), ToStringBuilder.reflectionToString(vlanIfListResponse.getVlanIfList()));
            continue;
          }

          updateIfInfoEcAllMap(fcNode, clusterId, ifListResponse.getIfs(), vlanIfListResponse.getVlanIfList(),
              ifInfoEcMap, ifInfoEcAllMap, ifs);

          if (!NodeStatus.IN_SERVICE.getMessage().equals(node.getNodeState())) {
            updateAllIfStateToDown(node.getNodeId(), ifInfoEcMap);
            updateAllIfStateToDown(node.getNodeId(), ifInfoEcAllMap);
          }
        }

        List<FailureStatusNodeFailureEntity> nodes = getNodesStatus(fcNodeMap, ecListResponse.getNodeList(), clusterId);

        FailureStatusSliceUnitEntity sliceEntity = createSliceNotifyInfo(session, ifInfoEcMap, ifInfoEcAllMap);

        Map<ClusterType, Map<String, FailureStatus>> clusterFailureMap = createClusterNotifyInfo(session, ifInfoEcMap,
            ifInfoEcAllMap);
        List<FailureStatusClusterFailureEntity> clusters = getClusterFailureEntityList(clusterFailureMap, clusterId);

        RestResponseBase responseBase = createFailureReadListResponse(nodes, ifs, clusters, sliceEntity);
        return responseBase;

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        session.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateIfInfoEcAllMap(FcNode fcNode, int clusterId, InterfacesEcEntity ifEcEntity,
      List<VlanIfEcEntity> vlanEcEntity, Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcAllMap,
      List<FailureStatusIfFailureEntity> physicalIfEntityList) {

    String ecNodeId = fcNode.getEcNodeId().toString();

    for (PhysicalIfEcEntity physicalIf : ifEcEntity.getPhysicalIfList()) {
      LogicalIfStatusIfEntity logicalIfPhysical = getLogicalIfFromPhysicalIf(physicalIf, ecNodeId);
      FailureStatusIfFailureEntity ifFailureEntity = getIfFailureEntity(logicalIfPhysical, fcNode, clusterId);
      if (ifFailureEntity == null) {
        logger.info("Create lusterInfoMap and Failure notify skipped(Physical Unit :PhysicalIf)."
            + " IF state is empty. LogicalIfStatusIfEntity={0}.", logicalIfPhysical);
        continue;
      }
      setMap(ifInfoEcMap.get(InterfaceType.PHYSICAL_IF), logicalIfPhysical);
      setMap(ifInfoEcAllMap.get(InterfaceType.PHYSICAL_IF), logicalIfPhysical);
      physicalIfEntityList.add(ifFailureEntity);
    }
    for (BreakoutIfEcEntity breakoutIf : ifEcEntity.getBreakoutIfList()) {
      LogicalIfStatusIfEntity logicalIfBreakout = getLogicalIfFromBreakoutIf(breakoutIf, ecNodeId);
      setMap(ifInfoEcMap.get(InterfaceType.BREAKOUT_IF), logicalIfBreakout);
      setMap(ifInfoEcAllMap.get(InterfaceType.BREAKOUT_IF), logicalIfBreakout);
      physicalIfEntityList.add(getIfFailureEntity(logicalIfBreakout, fcNode, clusterId));
    }
    for (LagIfEcEntity lagIf : ifEcEntity.getLagIfList()) {
      LogicalIfStatusIfEntity logicalIfLag = getLogicalIfFromLagIf(lagIf, ecNodeId);
      setMap(ifInfoEcMap.get(InterfaceType.LAG_IF), logicalIfLag);
      setMap(ifInfoEcAllMap.get(InterfaceType.LAG_IF), logicalIfLag);
      physicalIfEntityList.add(getIfFailureEntity(logicalIfLag, fcNode, clusterId));
    }

    sortFailureStatusIfFailureEntity(physicalIfEntityList);
    if (vlanEcEntity == null) {
      return;
    }
    for (VlanIfEcEntity vlanIf : vlanEcEntity) {
      InterfaceOperationStatus vlanIfStatus = InterfaceOperationStatus.getEnumFromMessage(vlanIf.getIfState());
      if (InterfaceOperationStatus.UP != vlanIfStatus && InterfaceOperationStatus.DOWN != vlanIfStatus) {

        continue;
      }
      LogicalIfStatusIfEntity logicalIfVlan = getLogicalIfFromVlanIf(vlanIf, ecNodeId);
      setMap(ifInfoEcMap.get(InterfaceType.VLAN_IF), logicalIfVlan);
      setMap(ifInfoEcAllMap.get(InterfaceType.VLAN_IF), logicalIfVlan);
    }
  }

  private RestResponseBase createFailureReadListResponse(List<FailureStatusNodeFailureEntity> nodes,
      List<FailureStatusIfFailureEntity> ifs, List<FailureStatusClusterFailureEntity> clusters,
      FailureStatusSliceUnitEntity sliceEntity) {
    FailureStatusReadListResponseBody body = new FailureStatusReadListResponseBody();

    if (!nodes.isEmpty() || !ifs.isEmpty()) {
      FailureStatusPhysicalUnitEntity physicalUnit = new FailureStatusPhysicalUnitEntity();
      if (!nodes.isEmpty()) {
        physicalUnit.setNodeList(nodes);
      }
      if (!ifs.isEmpty()) {
        physicalUnit.setIfList(ifs);
      }
      body.setPhysicalUnit(physicalUnit);
    }

    if (!clusters.isEmpty()) {
      FailureStatusClusterUnitEntity clusterUnit = new FailureStatusClusterUnitEntity();
      clusterUnit.setClusterList(clusters);
      body.setClusterUnit(clusterUnit);
    }

    body.setSliceUnit(sliceEntity);

    return createRestResponse(body, HttpStatus.OK_200);
  }

  private List<FailureStatusNodeFailureEntity> getNodesStatus(Map<String, FcNode> fcNodeMap,
      List<NodeEcEntity> allNodes, int clusterId) {

    List<FailureStatusNodeFailureEntity> nodes = new ArrayList<>();

    for (NodeEcEntity nodeEcEntity : allNodes) {

      FcNode fcNode = fcNodeMap.get(nodeEcEntity.getNodeId());
      if (fcNode == null) {
        logger.info("Failure status create skipped(Physical Unit :Nodes). Target Node not found. NodeEcEntity={0}.",
            nodeEcEntity);
        continue;
      }

      String nodeType = fcNode.getNodeTypeEnum().getSingularMessage();
      Integer nodeId = fcNode.getNodeId();

      if (NodeStatus.IN_SERVICE.getMessage().equals(nodeEcEntity.getNodeState())) {
        nodes.add(getNodeFailureEntity(clusterId, nodeType, FailureStatus.UP, nodeId));
      } else {
        nodes.add(getNodeFailureEntity(clusterId, nodeType, FailureStatus.DOWN, nodeId));
      }
    }

    sortFailureStatusNodeFailureEntity(nodes);
    return nodes;
  }

  private FailureStatusNodeFailureEntity getNodeFailureEntity(int clusterId, String fabricType, FailureStatus status,
      Integer nodeId) {

    FailureStatusNodeFailureEntity node = new FailureStatusNodeFailureEntity();
    node.setClusterId(String.valueOf(clusterId));
    node.setFabricType(fabricType);
    node.setFailureStatusEnum(status);
    node.setNodeId(nodeId.toString());
    return node;
  }

}
