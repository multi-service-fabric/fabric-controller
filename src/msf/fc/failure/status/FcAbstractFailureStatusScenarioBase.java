
package msf.fc.failure.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcClusterLinkIfDao;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.failure.search.FcFailurePathSearcher;
import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.InterfaceOperationStatus;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusIfEntity;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusNodeEntity;
import msf.mfcfc.failure.status.data.AbstractFailureStatusScenarioBase;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of CP traffic information
 * acquisition in traffic management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits RestRequestBase class
 */
public abstract class FcAbstractFailureStatusScenarioBase<T extends RestRequestBase>
    extends AbstractFailureStatusScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractFailureStatusScenarioBase.class);

  private Map<LogicalIfStatusIfEntity, FcClusterLinkIf> clusterLinkMap = new HashMap<>();

  private Map<LogicalIfStatusIfEntity, FcEdgePoint> edgePointMap = new HashMap<>();

  private Map<LogicalIfStatusIfEntity, FcInternalLinkIf> internalLinkMap = new HashMap<>();

  private Map<LogicalIfStatusIfEntity, FcL2Cp> l2CpMap = new HashMap<>();

  private Map<LogicalIfStatusIfEntity, FcL3Cp> l3CpMap = new HashMap<>();

  private Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMap = null;

  private int downLinks = 0;

  protected Map<ClusterType, Map<String, FailureStatus>> createClusterNotifyInfo(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMap) throws MsfException {

    try {
      logger.methodStart();

      createAllIfMap(session, ifInfoEcMap);

      if (internalLinkMap.size() > 0 && ifInfoAllMap == null) {

        if (this.ifInfoAllMap != null) {
          ifInfoAllMap = this.ifInfoAllMap;
        } else {
          ifInfoAllMap = getIfInfoAllMap(false);
        }
      }

      Map<ClusterType, Map<String, FailureStatus>> clusterFailureMap = new HashMap<>();

      Map<String, FailureStatus> edgePointFailureL3CpMap = getEdgePointFailureL3CpMap(session);
      clusterFailureMap.put(ClusterType.EDGE_POINT, edgePointFailureL3CpMap);

      Map<String, FailureStatus> edgePointFailureMap = getEdgePointFailureMap();
      clusterFailureMap.get(ClusterType.EDGE_POINT).putAll(edgePointFailureMap);

      Map<String, FailureStatus> internalFailureMap = getInternalFailureMap(session, ifInfoAllMap);
      clusterFailureMap.put(ClusterType.INTERNAL, internalFailureMap);

      Map<String, FailureStatus> clusterLinkFailureMap = getClusterLinkFailureMap();
      clusterFailureMap.put(ClusterType.CLUSTER_LINK_IF, clusterLinkFailureMap);

      return clusterFailureMap;

    } finally {
      logger.methodEnd();
    }
  }

  protected FailureStatusSliceUnitEntity createSliceNotifyInfo(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "ifInfoEcMap", "ifInfoAllMap" }, new Object[] { ifInfoEcMap, ifInfoAllMap });

      createAllIfMap(session, ifInfoEcMap);

      if (internalLinkMap.isEmpty() && clusterLinkMap.isEmpty() && l2CpMap.isEmpty() && l3CpMap.isEmpty()) {
        return null;
      }

      boolean isNotify = (ifInfoAllMap == null);

      if (isNotify) {
        ifInfoAllMap = getIfInfoAllMap(true);
        this.ifInfoAllMap = ifInfoAllMap;
        createAllIfMap(session, ifInfoAllMap);
      }

      Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> internalLinkPairMap = getInternalLinkPairMap(session,
          ifInfoAllMap);

      FcFailurePathSearcher searcher = new FcFailurePathSearcher();
      FailureStatusSliceUnitEntity sliceUnitEntity = searcher.searchFailurePath(session, clusterLinkMap, l2CpMap,
          l3CpMap, internalLinkPairMap);

      if (isNotify) {

        Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMapBeforeNotify = createIfInfoMapBeforeNotify(
            ifInfoAllMap, ifInfoEcMap);

        createAllIfMap(session, ifInfoAllMapBeforeNotify);

        Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> internalLinkPairMapBeforeNotify = getInternalLinkPairMap(
            session, ifInfoAllMapBeforeNotify);

        FailureStatusSliceUnitEntity sliceUnitEntityBeforeNotify = searcher.searchFailurePath(session, clusterLinkMap,
            l2CpMap, l3CpMap, internalLinkPairMapBeforeNotify);

        FailureStatusSliceUnitEntity diffEntity = takeDiffBetweenBeforeAndAfterNotify(sliceUnitEntityBeforeNotify,
            sliceUnitEntity, true);
        return diffEntity;
      } else {
        return sliceUnitEntity;
      }
    } finally {
      logger.methodEnd();
    }
  }

  @SuppressWarnings("unchecked")
  private Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> createIfInfoMapBeforeNotify(
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMap,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap) {
    try {
      logger.methodStart(new String[] { "ifInfoAllMap", "ifInfoEcMap" }, new Object[] { ifInfoAllMap, ifInfoEcMap });

      if (ifInfoEcMap == null) {
        logger.debug("ifInfoEcMap is null");
        return ifInfoAllMap;
      }

      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMapCp = (Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>>) deepCopy(
          ifInfoAllMap);
      for (InterfaceType type : ifInfoEcMap.keySet()) {
        Map<String, List<LogicalIfStatusIfEntity>> statusMap = ifInfoEcMap.get(type);
        for (String ecNodeId : statusMap.keySet()) {

          List<LogicalIfStatusIfEntity> notifyStatusList = statusMap.get(ecNodeId);

          List<LogicalIfStatusIfEntity> statusList = ifInfoAllMapCp.get(type).get(ecNodeId);
          for (LogicalIfStatusIfEntity notifyIfStatus : notifyStatusList) {
            for (LogicalIfStatusIfEntity ifStatus : statusList) {

              if (isSameLogicalIf(ifStatus, notifyIfStatus)) {
                logger.debug("found IF same status as notification. IF = {0}", ifStatus);
                if (notifyIfStatus.getStatusEnum().equals(FailureStatus.UP)) {
                  ifStatus.setStatusEnum(FailureStatus.DOWN);
                } else {
                  ifStatus.setStatusEnum(FailureStatus.UP);
                }
                logger.debug("after status = {0}", ifStatus.getStatus());
                break;
              }
            }
          }
        }
      }
      return ifInfoAllMapCp;
    } finally {
      logger.methodEnd();
    }
  }

  private boolean isSameLogicalIf(LogicalIfStatusIfEntity logicalIfStatus1, LogicalIfStatusIfEntity logicalIfStatus2) {
    if (logicalIfStatus1.getNodeId().equals(logicalIfStatus2.getNodeId())
        && logicalIfStatus1.getIfType().equals(logicalIfStatus2.getIfType())
        && logicalIfStatus1.getIfId().equals(logicalIfStatus2.getIfId())) {
      return true;
    } else {
      return false;
    }
  }

  private void createAllIfMap(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap) throws MsfException {

    clearMaps();

    createClusterTypeMap(session, ifInfoEcMap);

    Map<String, List<LogicalIfStatusIfEntity>> vlanIfMap = ifInfoEcMap.get(InterfaceType.VLAN_IF);
    createClusterTypeVlanMap(session, vlanIfMap);
  }

  private void createClusterTypeMap(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap) throws MsfException {

    Map<String, List<LogicalIfStatusIfEntity>> phyIfMap = ifInfoEcMap.get(InterfaceType.PHYSICAL_IF);
    Map<String, List<LogicalIfStatusIfEntity>> breIfMap = ifInfoEcMap.get(InterfaceType.BREAKOUT_IF);
    Map<String, List<LogicalIfStatusIfEntity>> lagIfMap = ifInfoEcMap.get(InterfaceType.LAG_IF);

    for (Map.Entry<String, List<LogicalIfStatusIfEntity>> entry : phyIfMap.entrySet()) {
      Integer ecNodeId = Integer.valueOf(entry.getKey());
      updateClusterTypeMapFromIf(session, ecNodeId, entry.getValue());
    }
    for (Map.Entry<String, List<LogicalIfStatusIfEntity>> entry : breIfMap.entrySet()) {
      Integer ecNodeId = Integer.valueOf(entry.getKey());
      updateClusterTypeMapFromIf(session, ecNodeId, entry.getValue());
    }
    for (Map.Entry<String, List<LogicalIfStatusIfEntity>> entry : lagIfMap.entrySet()) {
      Integer ecNodeId = Integer.valueOf(entry.getKey());
      updateClusterTypeMapFromIf(session, ecNodeId, entry.getValue());
    }
  }

  private void updateClusterTypeMapFromIf(SessionWrapper session, Integer ecNodeId,
      List<LogicalIfStatusIfEntity> logicalIfs) throws MsfException {
    for (LogicalIfStatusIfEntity logicalIf : logicalIfs) {
      FcEdgePoint edgePoint = getEdgePoint(session, logicalIf.getIfType(), ecNodeId, logicalIf.getIfId());
      if (edgePoint != null) {
        edgePointMap.put(logicalIf, edgePoint);
        continue;
      }
      FcClusterLinkIf clusterLink = getClusterLinkIf(session, logicalIf.getIfType(), ecNodeId, logicalIf.getIfId());
      if (clusterLink != null) {
        clusterLinkMap.put(logicalIf, clusterLink);
        continue;
      }
      FcInternalLinkIf internalLink = getInternalLinkIf(session, logicalIf.getIfType(), ecNodeId, logicalIf.getIfId());
      if (internalLink != null) {
        internalLinkMap.put(logicalIf, internalLink);
        continue;
      }
    }
  }

  private void createClusterTypeVlanMap(SessionWrapper session, Map<String, List<LogicalIfStatusIfEntity>> vlanIfMap)
      throws MsfException {

    for (Map.Entry<String, List<LogicalIfStatusIfEntity>> entry : vlanIfMap.entrySet()) {
      Integer ecNodeId = Integer.valueOf(entry.getKey());
      updateClusterTypeMapFromVlan(session, ecNodeId, entry.getValue());
    }
  }

  private void updateClusterTypeMapFromVlan(SessionWrapper session, Integer ecNodeId,
      List<LogicalIfStatusIfEntity> logicalIfs) throws MsfException {

    FcL2CpDao fcL2CpDao = new FcL2CpDao();
    FcL3CpDao fcL3CpDao = new FcL3CpDao();

    for (LogicalIfStatusIfEntity logicalIf : logicalIfs) {
      FcL2Cp l2Cp = fcL2CpDao.read(session, ecNodeId, logicalIf.getIfId());
      if (null != l2Cp) {
        l2CpMap.put(logicalIf, l2Cp);
      } else {
        FcL3Cp l3Cp = fcL3CpDao.read(session, ecNodeId, logicalIf.getIfId());
        if (null != l3Cp) {
          l3CpMap.put(logicalIf, l3Cp);
        }
      }
    }
  }

  private Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> getIfInfoAllMap(boolean getVlanIf)
      throws MsfException {

    Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoAllMap = new HashMap<>();
    ifInfoAllMap.put(InterfaceType.PHYSICAL_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
    ifInfoAllMap.put(InterfaceType.BREAKOUT_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
    ifInfoAllMap.put(InterfaceType.LAG_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
    ifInfoAllMap.put(InterfaceType.VLAN_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());

    NodeReadListEcResponseBody nodeListResponse = sendNodeReadList();

    for (NodeEcEntity node : nodeListResponse.getNodeList()) {

      String ecNodeId = node.getNodeId();
      InterfaceReadListEcResponseBody ifListResponse = sendIfReadList(ecNodeId);

      for (PhysicalIfEcEntity physicalIf : ifListResponse.getIfs().getPhysicalIfList()) {
        LogicalIfStatusIfEntity logicalIfPhysical = getLogicalIfFromPhysicalIf(physicalIf, ecNodeId);
        setMap(ifInfoAllMap.get(InterfaceType.PHYSICAL_IF), logicalIfPhysical);
      }
      for (BreakoutIfEcEntity breakoutIf : ifListResponse.getIfs().getBreakoutIfList()) {
        LogicalIfStatusIfEntity logicalIfBreakout = getLogicalIfFromBreakoutIf(breakoutIf, ecNodeId);
        setMap(ifInfoAllMap.get(InterfaceType.BREAKOUT_IF), logicalIfBreakout);
      }
      for (LagIfEcEntity lagIf : ifListResponse.getIfs().getLagIfList()) {
        LogicalIfStatusIfEntity logicalIfLag = getLogicalIfFromLagIf(lagIf, ecNodeId);
        setMap(ifInfoAllMap.get(InterfaceType.LAG_IF), logicalIfLag);
      }

      if (getVlanIf) {

        VlanIfReadListEcResponseBody vlanIfListResponse = sendVlanIfReadList(ecNodeId);
        for (VlanIfEcEntity vlanIf : vlanIfListResponse.getVlanIfList()) {
          InterfaceOperationStatus vlanIfStatus = InterfaceOperationStatus.getEnumFromMessage(vlanIf.getIfState());
          if (InterfaceOperationStatus.UP != vlanIfStatus && InterfaceOperationStatus.DOWN != vlanIfStatus) {

            continue;
          }
          LogicalIfStatusIfEntity logicalIfVlan = getLogicalIfFromVlanIf(vlanIf, ecNodeId);
          setMap(ifInfoAllMap.get(InterfaceType.VLAN_IF), logicalIfVlan);
        }
      }

      if (!NodeStatus.IN_SERVICE.getMessage().equals(node.getNodeState())) {
        updateAllIfStateToDown(node.getNodeId(), ifInfoAllMap);
      }
    }
    return ifInfoAllMap;
  }

  protected LogicalIfStatusIfEntity getLogicalIfFromPhysicalIf(PhysicalIfEcEntity physicalIf, String ecNodeId) {
    return getLogicalIf(physicalIf.getPhysicalIfId(), physicalIf.getIfState(), ecNodeId, InterfaceType.PHYSICAL_IF);
  }

  protected LogicalIfStatusIfEntity getLogicalIfFromBreakoutIf(BreakoutIfEcEntity breakoutIf, String ecNodeId) {
    return getLogicalIf(breakoutIf.getBreakoutIfId(), breakoutIf.getIfState(), ecNodeId, InterfaceType.BREAKOUT_IF);
  }

  protected LogicalIfStatusIfEntity getLogicalIfFromLagIf(LagIfEcEntity lagIf, String ecNodeId) {
    return getLogicalIf(lagIf.getLagIfId(), lagIf.getIfState(), ecNodeId, InterfaceType.LAG_IF);
  }

  protected LogicalIfStatusIfEntity getLogicalIfFromVlanIf(VlanIfEcEntity vlanIf, String ecNodeId) {
    return getLogicalIf(vlanIf.getVlanIfId(), vlanIf.getIfState(), ecNodeId, InterfaceType.VLAN_IF);
  }

  protected LogicalIfStatusIfEntity getLogicalIf(String ifId, String ifState, String ecNodeId, InterfaceType type) {

    LogicalIfStatusIfEntity entity = new LogicalIfStatusIfEntity();
    entity.setNodeId(ecNodeId);
    entity.setIfTypeEnum(type);
    entity.setIfId(ifId);
    entity.setStatus(ifState);
    return entity;
  }

  protected void setMapList(Map<String, List<LogicalIfStatusIfEntity>> failureInfoMap,
      List<LogicalIfStatusIfEntity> ifEntityList) {

    for (LogicalIfStatusIfEntity logicalIf : ifEntityList) {
      setMap(failureInfoMap, logicalIf);
    }
  }

  protected void setMap(Map<String, List<LogicalIfStatusIfEntity>> failureInfoMap, LogicalIfStatusIfEntity ifEntity) {

    String ecNodeId = ifEntity.getNodeId();
    if (failureInfoMap.get(ecNodeId) == null) {
      failureInfoMap.put(ecNodeId, new ArrayList<LogicalIfStatusIfEntity>());
    }
    failureInfoMap.get(ecNodeId).add(ifEntity);
  }

  private Map<String, FailureStatus> getEdgePointFailureL3CpMap(SessionWrapper session) throws MsfException {

    Map<String, FailureStatus> failureInfo = new HashMap<>();

    Map<Long, List<VlanIfEcEntity>> fcNodeMap = new HashMap<>();

    for (Entry<LogicalIfStatusIfEntity, FcL3Cp> entry : l3CpMap.entrySet()) {

      String edgePointId = String.valueOf(entry.getValue().getEdgePoint().getEdgePointId());

      if (FailureStatus.DOWN.equals(entry.getKey().getStatusEnum())) {
        failureInfo.put(edgePointId, FailureStatus.DOWN);
      } else if (FailureStatus.UP.equals(entry.getKey().getStatusEnum())) {

        FailureStatus failureStatus = getFailureStatusEdgePoint(session, fcNodeMap,
            entry.getValue().getEdgePoint().getL3Cps(), entry.getValue().getVlanIf().getId().getVlanIfId());
        if (failureStatus == null) {
          logger.info(
              "Failure notify skipped(EdgePoint). Target IF not found in the EC system. logicalIfStatusIfEntity={0}.",
              entry.getKey());
          continue;
        }
        failureInfo.put(edgePointId, failureStatus);
      }
    }
    return failureInfo;
  }

  private FailureStatus getFailureStatusEdgePoint(SessionWrapper session, Map<Long, List<VlanIfEcEntity>> fcNodeMap,
      List<FcL3Cp> fcL3CpList, Integer vlanIfId) throws MsfException {

    List<VlanIfEcEntity> targetVlanIfList = new ArrayList<>();

    for (FcL3Cp fcL3Cp : fcL3CpList) {
      Long nodeInfoId = new Long(fcL3Cp.getVlanIf().getId().getNodeInfoId());
      Integer cpVlanIfId = fcL3Cp.getVlanIf().getId().getVlanIfId();

      if (!fcNodeMap.containsKey(nodeInfoId)) {

        FcNodeDao fcNodeDao = new FcNodeDao();
        FcNode fcNode = fcNodeDao.read(session, nodeInfoId);
        String ecNodeId = String.valueOf(fcNode.getEcNodeId());

        VlanIfReadListEcResponseBody responseBody = sendVlanIfReadList(ecNodeId);
        List<VlanIfEcEntity> vlanIfEcList = responseBody.getVlanIfList();
        if (vlanIfEcList == null) {
          vlanIfEcList = new ArrayList<>();
        }
        fcNodeMap.put(nodeInfoId, vlanIfEcList);
      }

      List<VlanIfEcEntity> vlanIfList = fcNodeMap.get(nodeInfoId);
      VlanIfEcEntity vlanIf = getTargetVlanIf(vlanIfList, cpVlanIfId);
      if (vlanIf != null) {
        targetVlanIfList.add(vlanIf);
      } else {
        logger.debug("There is only in the FC system.(L3Cp:VlanIF) nodeInfoId={0}, vlanIfId={1}.", nodeInfoId,
            cpVlanIfId);
      }
    }

    return getReturnFailureStatus(vlanIfId.toString(), targetVlanIfList);
  }

  private VlanIfEcEntity getTargetVlanIf(List<VlanIfEcEntity> vlanIfNodeInfo, Integer vlanIfId) {

    for (VlanIfEcEntity vlanIf : vlanIfNodeInfo) {
      if (vlanIfId.toString().equals(vlanIf.getVlanIfId())) {
        return vlanIf;
      }
    }
    return null;
  }

  private FailureStatus getReturnFailureStatus(String vlanIfId, List<VlanIfEcEntity> targetVlanIfList) {

    boolean isNotifyIf = false;
    boolean isDown = false;
    for (VlanIfEcEntity vlanIf : targetVlanIfList) {
      if (vlanIf.getVlanIfId().equals(String.valueOf(vlanIfId))) {
        isNotifyIf = true;
      }
      if (InterfaceOperationStatus.UP != InterfaceOperationStatus.getEnumFromMessage(vlanIf.getIfState())) {

        isDown = true;
      }
    }
    if (!isNotifyIf) {

      return null;
    }
    return isDown ? FailureStatus.DOWN : FailureStatus.UP;
  }

  private Map<String, FailureStatus> getEdgePointFailureMap() {

    Map<String, FailureStatus> failureInfo = new HashMap<>();
    for (Entry<LogicalIfStatusIfEntity, FcEdgePoint> entry : edgePointMap.entrySet()) {

      String edgePointId = String.valueOf(entry.getValue().getEdgePointId());
      FailureStatus status = entry.getKey().getStatusEnum();
      failureInfo.put(edgePointId, status);
    }
    return failureInfo;
  }

  private Map<String, FailureStatus> getInternalFailureMap(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> infoAllMap) throws MsfException {

    Map<String, FailureStatus> failureInfo = new HashMap<>();

    if (internalLinkMap.size() == 0) {
      return failureInfo;
    }

    if (!isUpdateTargetIfInfo(infoAllMap)) {
      return failureInfo;
    }

    Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> internalLinkPairMap = getInternalLinkPairMap(session,
        infoAllMap);

    if (downLinks == 0) {
      failureInfo.put("", FailureStatus.UP);
    } else if (downLinks == internalLinkPairMap.size()) {
      failureInfo.put("", FailureStatus.DOWN);
    } else {
      failureInfo.put("", FailureStatus.WARN);
    }

    return failureInfo;
  }

  private boolean isUpdateTargetIfInfo(Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> infoAllMap) {

    for (LogicalIfStatusIfEntity updateLogicalIf : internalLinkMap.keySet()) {
      InterfaceType updateLogicalIfType = updateLogicalIf.getIfTypeEnum();
      String updateLogicalIfNodeId = updateLogicalIf.getNodeId();
      String updateLogicalIfId = updateLogicalIf.getIfId();

      if (!infoAllMap.get(updateLogicalIfType).containsKey(updateLogicalIfNodeId)) {
        logger.info(
            "Failure notify skipped(Internal). Target IF not found in the EC system. logicalIfStatusIfEntity={0}.",
            updateLogicalIf);
        return false;
      }

      List<LogicalIfStatusIfEntity> lists = infoAllMap.get(updateLogicalIfType).get(updateLogicalIfNodeId);
      boolean isFoundTarget = false;
      for (LogicalIfStatusIfEntity logicalIf : lists) {
        if (logicalIf.getIfId().equals(updateLogicalIfId)) {
          isFoundTarget = true;
          break;
        }
      }
      if (!isFoundTarget) {
        logger.info(
            "Failure notify skipped(Internal). Target IF not found in the EC system. logicalIfStatusIfEntity={0}.",
            updateLogicalIf);
        return false;
      }
    }
    return true;
  }

  private Map<LogicalIfStatusIfEntity, FcInternalLinkIf> getIfInfoOnInternal(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> infoAllMap) throws MsfException {

    List<LogicalIfStatusIfEntity> allIfList = new ArrayList<>();
    for (List<LogicalIfStatusIfEntity> logicalIfs : infoAllMap.get(InterfaceType.PHYSICAL_IF).values()) {
      if (logicalIfs != null) {
        allIfList.addAll(logicalIfs);
      }
    }
    for (List<LogicalIfStatusIfEntity> logicalIfs : infoAllMap.get(InterfaceType.BREAKOUT_IF).values()) {
      if (logicalIfs != null) {
        allIfList.addAll(logicalIfs);
      }
    }
    for (List<LogicalIfStatusIfEntity> logicalIfs : infoAllMap.get(InterfaceType.LAG_IF).values()) {
      if (logicalIfs != null) {
        allIfList.addAll(logicalIfs);
      }
    }

    Map<LogicalIfStatusIfEntity, FcInternalLinkIf> internalLinkMap = new HashMap<>();
    for (LogicalIfStatusIfEntity logicalIf : allIfList) {
      Integer ecNodeId = new Integer(logicalIf.getNodeId());
      FcInternalLinkIf internalLink = getInternalLinkIf(session, logicalIf.getIfType(), ecNodeId, logicalIf.getIfId());
      if (internalLink != null) {
        internalLinkMap.put(logicalIf, internalLink);
        continue;
      }
    }

    return internalLinkMap;
  }

  private Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> getInternalLinkPairMap(SessionWrapper session,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> infoAllMap) throws MsfException {

    Map<LogicalIfStatusIfEntity, FcInternalLinkIf> internalLinkMap = getIfInfoOnInternal(session, infoAllMap);

    Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> pairMap = new HashMap<>();

    for (Entry<LogicalIfStatusIfEntity, FcInternalLinkIf> entry : internalLinkMap.entrySet()) {
      List<FcInternalLinkIf> oppositeIfList = entry.getValue().getOppositeInternalLinkIfs();
      if (oppositeIfList.size() == 0) {

        continue;
      }

      FcInternalLinkIf oppositIf = oppositeIfList.get(0);
      for (Entry<LogicalIfStatusIfEntity, FcInternalLinkIf> opEntry : internalLinkMap.entrySet()) {
        if (oppositIf.getInternalLinkIfId().equals(opEntry.getValue().getInternalLinkIfId())) {
          if (!pairMap.containsValue(entry.getKey())) {
            pairMap.put(entry.getKey(), opEntry.getKey());
          }
          break;
        }
      }
    }

    downLinks = getInternalLinkDownCount(pairMap);

    return pairMap;
  }

  private int getInternalLinkDownCount(Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> internalLinkPairMap) {

    int cntDown = 0;
    for (Entry<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> pairEntry : internalLinkPairMap.entrySet()) {

      if (FailureStatus.DOWN == pairEntry.getKey().getStatusEnum()) {
        cntDown++;
      } else if (FailureStatus.DOWN == pairEntry.getValue().getStatusEnum()) {
        cntDown++;
      }
    }
    return cntDown;
  }

  private Map<String, FailureStatus> getClusterLinkFailureMap() {

    Map<String, FailureStatus> failureInfo = new HashMap<>();
    for (Entry<LogicalIfStatusIfEntity, FcClusterLinkIf> entry : clusterLinkMap.entrySet()) {

      String clusterLinkId = String.valueOf(entry.getValue().getClusterLinkIfId());
      FailureStatus status = entry.getKey().getStatusEnum();
      failureInfo.put(clusterLinkId, status);
    }
    return failureInfo;
  }

  private FcEdgePoint getEdgePoint(SessionWrapper session, String ifType, Integer ecNodeId, String ifId)
      throws MsfException {

    FcEdgePoint fcEdgePoint = null;
    FcEdgePointDao fcEdgePointDao = new FcEdgePointDao();
    if (InterfaceType.PHYSICAL_IF.getMessage().equals(ifType)) {
      fcEdgePoint = fcEdgePointDao.readByPhysicalIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.LAG_IF.getMessage().equals(ifType)) {
      fcEdgePoint = fcEdgePointDao.readByLagIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.BREAKOUT_IF.getMessage().equals(ifType)) {
      fcEdgePoint = fcEdgePointDao.readByBreakoutIfId(session, ecNodeId, ifId);
    }
    return fcEdgePoint;
  }

  private FcInternalLinkIf getInternalLinkIf(SessionWrapper session, String ifType, Integer ecNodeId, String ifId)
      throws MsfException {

    FcInternalLinkIf fcInternalLinkIf = null;
    FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
    if (InterfaceType.PHYSICAL_IF.getMessage().equals(ifType)) {
      fcInternalLinkIf = fcInternalLinkIfDao.readByPhysicalIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.LAG_IF.getMessage().equals(ifType)) {
      fcInternalLinkIf = fcInternalLinkIfDao.readByLagIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.BREAKOUT_IF.getMessage().equals(ifType)) {
      fcInternalLinkIf = fcInternalLinkIfDao.readByBreakoutIfId(session, ecNodeId, ifId);
    }
    return fcInternalLinkIf;
  }

  private FcClusterLinkIf getClusterLinkIf(SessionWrapper session, String ifType, Integer ecNodeId, String ifId)
      throws MsfException {

    FcClusterLinkIf fcClusterLinkIf = null;
    FcClusterLinkIfDao fcClusterLinkIfDao = new FcClusterLinkIfDao();
    if (InterfaceType.PHYSICAL_IF.getMessage().equals(ifType)) {
      fcClusterLinkIf = fcClusterLinkIfDao.readByPhysicalIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.LAG_IF.getMessage().equals(ifType)) {
      fcClusterLinkIf = fcClusterLinkIfDao.readByLagIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.BREAKOUT_IF.getMessage().equals(ifType)) {
      fcClusterLinkIf = fcClusterLinkIfDao.readByBreakoutIfId(session, ecNodeId, ifId);
    }
    return fcClusterLinkIf;
  }

  protected FailureStatusNodeFailureEntity getNodeFailureEntity(LogicalIfStatusNodeEntity logicalIf, FcNode fcNode,
      int clusterId) {
    FailureStatusNodeFailureEntity node = new FailureStatusNodeFailureEntity();
    node.setClusterId(String.valueOf(clusterId));
    node.setFabricType(fcNode.getNodeTypeEnum().getSingularMessage());
    node.setNodeId(String.valueOf(fcNode.getNodeId()));
    node.setFailureStatusEnum(logicalIf.getFailureStatusEnum());
    return node;
  }

  protected FailureStatusIfFailureEntity getIfFailureEntity(LogicalIfStatusIfEntity logicalIf, FcNode fcNode,
      int clusterId) {

    if (StringUtils.isEmpty(logicalIf.getStatus())) {

      return null;
    }

    FailureStatusIfFailureEntity ife = new FailureStatusIfFailureEntity();
    ife.setClusterId(String.valueOf(clusterId));
    ife.setFabricType(fcNode.getNodeTypeEnum().getSingularMessage());
    ife.setNodeId(String.valueOf(fcNode.getNodeId()));
    ife.setIfTypeEnum(logicalIf.getIfTypeEnum());
    ife.setIfId(logicalIf.getIfId());
    ife.setFailureStatusEnum(logicalIf.getStatusEnum());
    return ife;
  }

  protected List<FailureStatusClusterFailureEntity> getClusterFailureEntityList(
      Map<ClusterType, Map<String, FailureStatus>> failureInfoMap, int clusterId) {

    List<FailureStatusClusterFailureEntity> clusterList = new ArrayList<>();
    Map<String, FailureStatus> clusterMap = failureInfoMap.get(ClusterType.CLUSTER_LINK_IF);
    if (clusterMap != null) {

      clusterList.addAll(getClusterFailureEntityList(clusterMap, clusterId, ClusterType.CLUSTER_LINK_IF));
    }

    Map<String, FailureStatus> edgePointMap = failureInfoMap.get(ClusterType.EDGE_POINT);
    if (edgePointMap != null) {

      clusterList.addAll(getClusterFailureEntityList(edgePointMap, clusterId, ClusterType.EDGE_POINT));
    }

    Map<String, FailureStatus> internalMap = failureInfoMap.get(ClusterType.INTERNAL);
    if (internalMap != null) {

      clusterList.addAll(getClusterFailureEntityList(internalMap, clusterId, ClusterType.INTERNAL));
    }
    return clusterList;
  }

  private List<FailureStatusClusterFailureEntity> getClusterFailureEntityList(Map<String, FailureStatus> failureMap,
      int clusterId, ClusterType type) {

    List<FailureStatusClusterFailureEntity> clusterFailureList = new ArrayList<>();
    for (Map.Entry<String, FailureStatus> failure : failureMap.entrySet()) {
      FailureStatusClusterFailureEntity clusterFailure = new FailureStatusClusterFailureEntity();
      clusterFailure.setClusterId(String.valueOf(clusterId));
      clusterFailure.setClusterTypeEnum(type);
      clusterFailure.setFailureStatusEnum(failure.getValue());

      clusterFailure.setId(failure.getKey().isEmpty() ? null : failure.getKey());
      clusterFailureList.add(clusterFailure);
    }
    return clusterFailureList;
  }

  protected NodeReadListEcResponseBody sendNodeReadList() throws MsfException {

    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ_LIST.getHttpMethod(),
          EcRequestUri.NODE_READ_LIST.getUri(), null, ecControlIpAddress, ecControlPort);

      NodeReadListEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          NodeReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          responseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected InterfaceReadListEcResponseBody sendIfReadList(String ecNodeId) throws MsfException {

    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.IF_READ_LIST.getHttpMethod(),
          EcRequestUri.IF_READ_LIST.getUri(ecNodeId), null, ecControlIpAddress, ecControlPort);

      InterfaceReadListEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          InterfaceReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          responseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected VlanIfReadListEcResponseBody sendVlanIfReadList(String ecNodeId) throws MsfException {

    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.VLAN_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.VLAN_IF_READ_LIST.getUri(ecNodeId), null, ecControlIpAddress, ecControlPort);

      VlanIfReadListEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          VlanIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          responseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {

    try {
      logger.methodStart(new String[] { "body", "statusCode" },
          new Object[] { ToStringBuilder.reflectionToString(body), statusCode });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected int getDownLinks() {
    return downLinks;
  }

  private void clearMaps() {
    clusterLinkMap.clear();
    edgePointMap.clear();
    internalLinkMap.clear();
    l2CpMap.clear();
    l3CpMap.clear();
  }

  protected void updateAllIfStateToDown(String ecNodeId,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoMap) {
    for (Map<String, List<LogicalIfStatusIfEntity>> failureInfoMap : ifInfoMap.values()) {
      if (failureInfoMap.containsKey(ecNodeId)) {
        for (LogicalIfStatusIfEntity ifEntity : failureInfoMap.get(ecNodeId)) {
          ifEntity.setStatusEnum(FailureStatus.DOWN);
        }
      }
    }
  }
}
