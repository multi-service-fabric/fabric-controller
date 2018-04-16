
package msf.mfc.failure.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SliceUnitReachableOppositeType;
import msf.mfcfc.common.constant.SliceUnitReachableStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.search.AbstractFailurePathSearcher;
import msf.mfcfc.failure.search.CpData;
import msf.mfcfc.failure.search.DiNode;
import msf.mfcfc.failure.search.DiNodePair;
import msf.mfcfc.failure.status.data.entity.FailureStatusReachableStatusFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkReachableStatusEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;

/**
 * Implementation class for the failure path identification process in MFC
 * failure management function.
 *
 * @author NTT
 *
 */
public class MfcFailurePathSearcher extends AbstractFailurePathSearcher {

  private static final String CP_COMBI_SEPARATOR = "/";

  private static final MsfLogger logger = MsfLogger.getInstance(MfcFailurePathSearcher.class);

  private Map<MfcClusterLinkIf, FailureStatus> clusterLinkMap = new HashMap<>();

  private Map<Integer, Map<CpData, SliceUnitReachableStatus>> clusterCpReachabilityMap = new HashMap<>();

  private FailureStatusSliceUnitEntity failureStatusSliceUnitEntityFromFc = new FailureStatusSliceUnitEntity();

  private Map<String, Map<String, Boolean>> l2CpCombinationMap = new HashMap<>();

  private Map<String, Map<String, Boolean>> l3CpCombinationMap = new HashMap<>();

  @Override
  protected void createAllNodes(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      List<MfcBLeafDiNode> mfcBLeafDiNodeList = createBLeafNodeList();

      diNodeSet.addAll(mfcBLeafDiNodeList);

      for (MfcBLeafDiNode diNode : mfcBLeafDiNodeList) {
        for (MfcClusterLinkIf mfcClusterLinkIf : diNode.getClusterLinkIfMap().keySet()) {

          if (clusterCpReachabilityMap.containsKey(mfcClusterLinkIf.getClusterLinkIfId())) {

            putCpReachability(diNode.getCpReachabilityMap(),
                clusterCpReachabilityMap.get(mfcClusterLinkIf.getClusterLinkIfId()));
          }
        }

        for (FailureStatusSliceClusterLinkReachableStatusEntity clusterLinkReachable : failureStatusSliceUnitEntityFromFc
            .getClusterLink().getReachableStatusList()) {
          for (MfcClusterLinkIf mfcClusterLinkIf : diNode.getClusterLinkIfMap().keySet()) {

            if (mfcClusterLinkIf.getClusterLinkIfId()
                .equals(Integer.valueOf(clusterLinkReachable.getClusterLinkIfId()))) {

              setBLeafReachableInSameCluster(diNode, mfcClusterLinkIf,
                  Integer.valueOf(clusterLinkReachable.getOppositeClusterLinkIfId()),
                  clusterLinkReachable.getReachableStatusEnum());
            } else if (mfcClusterLinkIf.getClusterLinkIfId()
                .equals(Integer.valueOf(clusterLinkReachable.getOppositeClusterLinkIfId()))) {

              setBLeafReachableInSameCluster(diNode, mfcClusterLinkIf,
                  Integer.valueOf(clusterLinkReachable.getClusterLinkIfId()),
                  clusterLinkReachable.getReachableStatusEnum());
            }
          }
        }
      }

      logger.debug(diNodeSet);

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void createAllEdges() throws MsfException {
    try {
      logger.methodStart();
      for (DiNode diNode : diNodeSet) {
        for (DiNode diNode2 : diNodeSet) {
          MfcBLeafDiNode mfcBLeafDiNode = (MfcBLeafDiNode) diNode;
          MfcBLeafDiNode mfcBLeafDiNode2 = (MfcBLeafDiNode) diNode2;

          if (mfcBLeafDiNode.getBleafReachabilityMap().containsKey(mfcBLeafDiNode2.getStrForNodeSpecify())
              && mfcBLeafDiNode.getBleafReachabilityMap().get(mfcBLeafDiNode2.getStrForNodeSpecify())
                  .equals(SliceUnitReachableStatus.REACHABLE)) {

            Double linkCost = getLinkCostFromCluterLinkIf(mfcBLeafDiNode2.getClusterId(),
                mfcBLeafDiNode2.getLeafNodeId(), mfcBLeafDiNode.getClusterLinkIfMap().keySet());
            if (linkCost != null) {
              createDiEdge(mfcBLeafDiNode, mfcBLeafDiNode2, linkCost);
            } else {
              createDiEdge(mfcBLeafDiNode, mfcBLeafDiNode2);
            }
          }
        }
      }

      logger.debug(diEdgeSet);
    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected void createStartGoalNodePairs() throws MsfException {
    try {
      logger.methodStart();

      for (DiNode diNode : diNodeSet) {
        MfcBLeafDiNode mfcBLeafDiNode = (MfcBLeafDiNode) diNode;

        if (mfcBLeafDiNode.getCpReachabilityMap().isEmpty()) {
          continue;
        }
        for (DiNode diNode2 : diNodeSet) {
          MfcBLeafDiNode mfcBLeafDiNode2 = (MfcBLeafDiNode) diNode2;

          if (mfcBLeafDiNode2.getCpReachabilityMap().isEmpty()) {
            continue;
          }

          if (mfcBLeafDiNode.getClusterId().equals(mfcBLeafDiNode2.getClusterId())) {
            continue;
          }

          boolean isExist = false;
          for (DiNodePair tmpPair : diNodePairSet) {

            if (tmpPair.getFrom().equals(diNode2) && tmpPair.getTo().equals(diNode)) {
              isExist = true;
              break;
            }
          }
          if (!isExist) {

            DiNodePair diNodePair = new DiNodePair(diNode, diNode2);

            calculatePath(diNodePair);
            diNodePairSet.add(diNodePair);
          }
        }
      }
      logger.debug("created node pairs = " + diNodePairSet);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void createAllReachableStatuses() throws MsfException {
    try {
      logger.methodStart();

      for (DiNodePair diNodePair : diNodePairSet) {
        MfcBLeafDiNode fromNode = (MfcBLeafDiNode) diNodePair.getFrom();
        MfcBLeafDiNode toNode = (MfcBLeafDiNode) diNodePair.getTo();
        for (Map.Entry<CpData, SliceUnitReachableStatus> fromEntry : fromNode.getCpReachabilityMap().entrySet()) {
          for (Map.Entry<CpData, SliceUnitReachableStatus> toEntry : toNode.getCpReachabilityMap().entrySet()) {

            if (fromEntry.getKey().getSliceType().equals(toEntry.getKey().getSliceType())
                && fromEntry.getKey().getSliceId().equals(toEntry.getKey().getSliceId())) {
              createFailureStatusEntity(fromEntry, toEntry, diNodePair.isReachable());
            }
          }
        }
      }

      for (FailureStatusSliceFailureEntity sliceFailureEntityFromFc : failureStatusSliceUnitEntityFromFc
          .getSliceList()) {

        if (sliceFailureEntityFromFc.getReachableStatusList() != null) {
          for (FailureStatusReachableStatusFailureEntity reachableStatusFailureFromFc : sliceFailureEntityFromFc
              .getReachableStatusList()) {

            if (reachableStatusFailureFromFc.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CP)) {
              addFailureStatusReachableStatusFailureEntity(sliceFailureEntityFromFc.getSliceTypeEnum(),
                  sliceFailureEntityFromFc.getSliceId(), reachableStatusFailureFromFc.getCpId(),
                  SliceUnitReachableOppositeType.CP, reachableStatusFailureFromFc.getOppositeId(),
                  reachableStatusFailureFromFc.getReachableStatusEnum());
              setTrueCpCombination(sliceFailureEntityFromFc.getSliceTypeEnum(), sliceFailureEntityFromFc.getSliceId(),
                  reachableStatusFailureFromFc.getCpId(), reachableStatusFailureFromFc.getOppositeId());
            }
          }
        }
      }

      createCpReachablesNotInPairs();

      checkDuplicateCpReachability(sliceList);

      failureStatusSliceUnitEntity.setSliceList(sliceList);
      failureStatusSliceUnitEntity.setClusterLink(clusterLink);
    } finally {
      logger.methodEnd();
    }

  }

  /**
   * Based on the given information, execute the failure path identification
   * process in a cluster and return the information of failure notification for
   * slice by slice.
   *
   * @param sessionWrapper
   *          Session management instance
   * @param clusterLinkMap
   *          Map to store the failure status of all inter-cluster link IFs
   *          (inter-cluster link IF information, IF failure information)
   * @param clusterCpReachabilityMap
   *          Map to store the reachability between inter-cluster link IFs and
   *          CP in the same cluster (inter-cluster link IFID, (CP information,
   *          reachability))
   * @param failureStatusSliceUnitEntityFromFc
   *          All the failure information for slice by slice before calculation
   *          in MFC that is acquired from each FC
   * @return information of failure notification for slice by slice that is
   *         recalculated in MFC
   * @throws MsfException
   *           When a DB access error occurs
   */
  public FailureStatusSliceUnitEntity searchFailurePath(SessionWrapper sessionWrapper,
      Map<MfcClusterLinkIf, FailureStatus> clusterLinkMap,
      Map<Integer, Map<CpData, SliceUnitReachableStatus>> clusterCpReachabilityMap,
      FailureStatusSliceUnitEntity failureStatusSliceUnitEntityFromFc) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "clusterLinkMap", "clusterCpReachabilityMap", "failureStatusSliceUnitEntityFromFc" },
          new Object[] { clusterLinkMap, clusterCpReachabilityMap, failureStatusSliceUnitEntityFromFc });
      this.clusterLinkMap = clusterLinkMap;
      this.clusterCpReachabilityMap = clusterCpReachabilityMap;
      this.failureStatusSliceUnitEntityFromFc = failureStatusSliceUnitEntityFromFc;

      init();

      createCpCombinationMap(sessionWrapper);

      createAllNodes(sessionWrapper);

      createAllEdges();

      createGraph();

      createStartGoalNodePairs();

      createAllReachableStatuses();

      logger.debug(failureStatusSliceUnitEntity);
      return failureStatusSliceUnitEntity;
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      throw exp;
    } finally {
      logger.methodEnd();
    }
  }

  private List<MfcBLeafDiNode> createBLeafNodeList() throws MsfException {

    Map<String, MfcBLeafDiNode> leafNodeMap = new HashMap<>();

    int topologyNodeId = 1;
    for (Map.Entry<MfcClusterLinkIf, FailureStatus> entry : clusterLinkMap.entrySet()) {

      Integer clusterId = entry.getKey().getSwCluster().getSwClusterId();

      Integer leafNodeId = getNodeIdFromClusterLinkIf(entry.getKey());

      Integer oppositeClusterId = entry.getKey().getOppositeSwClusterId();

      Integer oppositeLeafNodeId = getOppositeNodeIdFromClusterLinkIf(entry.getKey());

      MfcBLeafDiNode mfcBLeafDiNode = getMfcBLeafDiNodeFromClusterLinkIf(clusterId, leafNodeId, topologyNodeId,
          leafNodeMap);

      if (mfcBLeafDiNode.getNodeNo() == topologyNodeId) {

        topologyNodeId++;
      }

      MfcBLeafDiNode oppositeMfcBLeafDiNode = getMfcBLeafDiNodeFromClusterLinkIf(oppositeClusterId, oppositeLeafNodeId,
          topologyNodeId, leafNodeMap);

      if (oppositeMfcBLeafDiNode.getNodeNo() == topologyNodeId) {

        topologyNodeId++;
      }

      setReachableStatus(oppositeClusterId, oppositeLeafNodeId, mfcBLeafDiNode, entry.getValue());
      setReachableStatus(clusterId, leafNodeId, oppositeMfcBLeafDiNode, entry.getValue());

      mfcBLeafDiNode.getClusterLinkIfMap().put(entry.getKey(), entry.getValue());
    }
    return new ArrayList<>(leafNodeMap.values());
  }

  private Integer getNodeIdFromClusterLinkIf(MfcClusterLinkIf mfcClusterLinkIf) throws MsfException {
    if (mfcClusterLinkIf.getLagLink() != null) {
      return mfcClusterLinkIf.getLagLink().getNodeId();
    } else {
      return mfcClusterLinkIf.getPhysicalLink().getNodeId();
    }
  }

  private Integer getOppositeNodeIdFromClusterLinkIf(MfcClusterLinkIf mfcClusterLinkIf) throws MsfException {
    if (mfcClusterLinkIf.getLagLink() != null) {
      return mfcClusterLinkIf.getLagLink().getOppositeNodeId();
    } else {
      return mfcClusterLinkIf.getPhysicalLink().getOppositeNodeId();
    }
  }

  private MfcBLeafDiNode getMfcBLeafDiNodeFromClusterLinkIf(int clusterId, int leafNodeId, int topologyNodeId,
      Map<String, MfcBLeafDiNode> leafNodeMap) throws MsfException {

    String bleafNotifyStr = MfcBLeafDiNode.makeStrForBLeafSpecify(clusterId, leafNodeId);

    MfcBLeafDiNode mfcBLeafDiNode;
    if (leafNodeMap.containsKey(bleafNotifyStr)) {
      mfcBLeafDiNode = leafNodeMap.get(bleafNotifyStr);
    } else {
      mfcBLeafDiNode = new MfcBLeafDiNode(topologyNodeId);
      mfcBLeafDiNode.setClusterId(clusterId);
      mfcBLeafDiNode.setLeafNodeId(leafNodeId);
      leafNodeMap.put(bleafNotifyStr, mfcBLeafDiNode);
    }
    return mfcBLeafDiNode;
  }

  private void setReachableStatus(int oppositeClusterId, int oppositeLeafNodeId, MfcBLeafDiNode mfcBLeafDiNode,
      FailureStatus clusterLinkIfFailureStatus) throws MsfException {

    String opoositeBleafNotifyStr = MfcBLeafDiNode.makeStrForBLeafSpecify(oppositeClusterId, oppositeLeafNodeId);

    mfcBLeafDiNode.getBleafReachabilityMap().putIfAbsent(opoositeBleafNotifyStr, SliceUnitReachableStatus.REACHABLE);

    if (!clusterLinkIfFailureStatus.equals(FailureStatus.UP)) {
      mfcBLeafDiNode.getBleafReachabilityMap().put(opoositeBleafNotifyStr, SliceUnitReachableStatus.UNREACHABLE);
    }
  }

  private void setBLeafReachableInSameCluster(MfcBLeafDiNode diNode, MfcClusterLinkIf mfcClusterLinkIf,
      Integer clusterLinkIfId, SliceUnitReachableStatus reachableStatus) throws MsfException {

    for (MfcClusterLinkIf otherMfcClusterLinkIf : clusterLinkMap.keySet()) {

      if (otherMfcClusterLinkIf.getClusterLinkIfId().equals(clusterLinkIfId)) {

        if (!getNodeIdFromClusterLinkIf(mfcClusterLinkIf).equals(getNodeIdFromClusterLinkIf(otherMfcClusterLinkIf))) {
          String key = MfcBLeafDiNode.makeStrForBLeafSpecify(otherMfcClusterLinkIf.getSwCluster().getSwClusterId(),
              getNodeIdFromClusterLinkIf(otherMfcClusterLinkIf));

          diNode.getBleafReachabilityMap().putIfAbsent(key, SliceUnitReachableStatus.UNREACHABLE);

          if (reachableStatus.equals(SliceUnitReachableStatus.REACHABLE)) {
            diNode.getBleafReachabilityMap().put(key, SliceUnitReachableStatus.REACHABLE);
          }
        }
        break;
      }
    }
  }

  private Double getLinkCostFromCluterLinkIf(int oppositeClusterId, int oppositeLeafNodeId,
      Set<MfcClusterLinkIf> mfcClusterLinkIfSet) throws MsfException {
    for (MfcClusterLinkIf mfcClusterLinkIf : mfcClusterLinkIfSet) {
      int clusterId = mfcClusterLinkIf.getOppositeSwClusterId();
      int leafNodeId = getOppositeNodeIdFromClusterLinkIf(mfcClusterLinkIf);

      if (clusterId == oppositeClusterId && leafNodeId == oppositeLeafNodeId) {

        return mfcClusterLinkIf.getIgpCost().doubleValue();
      }
    }

    logger.debug("no match.");
    return null;
  }

  private void createFailureStatusEntity(Map.Entry<CpData, SliceUnitReachableStatus> fromEntry,
      Map.Entry<CpData, SliceUnitReachableStatus> toEntry, boolean existRoute) {

    SliceUnitReachableStatus reachableStatus = SliceUnitReachableStatus.REACHABLE;

    if (!existRoute || !fromEntry.getValue().equals(SliceUnitReachableStatus.REACHABLE)
        || !toEntry.getValue().equals(SliceUnitReachableStatus.REACHABLE)) {
      reachableStatus = SliceUnitReachableStatus.UNREACHABLE;
    }
    CpData fromCpData = fromEntry.getKey();
    CpData toCpData = toEntry.getKey();

    addFailureStatusReachableStatusFailureEntity(fromCpData.getSliceType(), fromCpData.getSliceId(),
        fromCpData.getCpId(), SliceUnitReachableOppositeType.CP, toCpData.getCpId(), reachableStatus);
    setTrueCpCombination(fromCpData.getSliceType(), fromCpData.getSliceId(), fromCpData.getCpId(), toCpData.getCpId());
  }

  private void createCpCombinationMap(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      l2CpCombinationMap.clear();
      l3CpCombinationMap.clear();

      MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();

      List<MfcL2Slice> l2SliceList = l2SliceDao.readList(sessionWrapper);
      for (MfcL2Slice l2Slice : l2SliceList) {
        Map<String, Boolean> cpCombinationMap = new HashMap<>();
        for (MfcL2Cp l2Cp1 : l2Slice.getL2Cps()) {
          for (MfcL2Cp l2Cp2 : l2Slice.getL2Cps()) {
            if (l2Cp1.getId().getCpId().compareTo(l2Cp2.getId().getCpId()) < 0) {
              cpCombinationMap.put(l2Cp1.getId().getCpId() + CP_COMBI_SEPARATOR + l2Cp2.getId().getCpId(), false);
            }
          }
        }
        l2CpCombinationMap.put(l2Slice.getSliceId(), cpCombinationMap);
      }

      MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();

      List<MfcL3Slice> l3SliceList = l3SliceDao.readList(sessionWrapper);
      for (MfcL3Slice l3Slice : l3SliceList) {
        Map<String, Boolean> cpCombinationMap = new HashMap<>();
        for (MfcL3Cp l3Cp1 : l3Slice.getL3Cps()) {
          for (MfcL3Cp l3Cp3 : l3Slice.getL3Cps()) {
            if (l3Cp1.getId().getCpId().compareTo(l3Cp3.getId().getCpId()) > 0) {
              cpCombinationMap.put(l3Cp1.getId().getCpId() + CP_COMBI_SEPARATOR + l3Cp3.getId().getCpId(), false);
            }
          }
        }
        l3CpCombinationMap.put(l3Slice.getSliceId(), cpCombinationMap);
      }
      logger.debug("l2Cp combinations :" + l2CpCombinationMap);
      logger.debug("l3Cp combinations :" + l3CpCombinationMap);
    } finally {
      logger.methodEnd();
    }
  }

  private void setTrueCpCombination(SliceType sliceType, String sliceId, String cp1, String cp2) {
    Map<String, Boolean> cpCombinationMap;
    if (SliceType.L2_SLICE.equals(sliceType)) {
      cpCombinationMap = l2CpCombinationMap.get(sliceId);
    } else {
      cpCombinationMap = l3CpCombinationMap.get(sliceId);
    }
    if (cpCombinationMap != null) {

      if (cpCombinationMap.containsKey(cp1 + CP_COMBI_SEPARATOR + cp2)) {
        cpCombinationMap.put(cp1 + CP_COMBI_SEPARATOR + cp2, true);
      } else if (cpCombinationMap.containsKey(cp2 + CP_COMBI_SEPARATOR + cp1)) {
        cpCombinationMap.put(cp2 + CP_COMBI_SEPARATOR + cp1, true);
      }
    }
  }

  private void createCpReachablesNotInPairs() {
    try {
      logger.methodStart();
      for (String sliceId : l2CpCombinationMap.keySet()) {
        createCpReachablesNotInPairs(SliceType.L2_SLICE, sliceId, l2CpCombinationMap.get(sliceId));
      }
      for (String sliceId : l3CpCombinationMap.keySet()) {
        createCpReachablesNotInPairs(SliceType.L3_SLICE, sliceId, l3CpCombinationMap.get(sliceId));
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void createCpReachablesNotInPairs(SliceType sliceType, String sliceId,
      Map<String, Boolean> cpCombinationMap) {
    for (String cpCombinationString : cpCombinationMap.keySet()) {

      if (!cpCombinationMap.get(cpCombinationString)) {
        addFailureStatusReachableStatusFailureEntity(sliceType, sliceId,
            cpCombinationString.split(CP_COMBI_SEPARATOR)[0], SliceUnitReachableOppositeType.CP,
            cpCombinationString.split(CP_COMBI_SEPARATOR)[1], SliceUnitReachableStatus.UNREACHABLE);
      }
    }
  }

  private void checkDuplicateCpReachability(List<FailureStatusSliceFailureEntity> sliceList) {
    try {
      logger.methodStart();
      for (FailureStatusSliceFailureEntity sliceEntity : sliceList) {
        if (sliceEntity.getReachableStatusList() != null) {
          Map<String, FailureStatusReachableStatusFailureEntity> reachableMap = new LinkedHashMap<>();

          for (FailureStatusReachableStatusFailureEntity reachableStatusEntity : sliceEntity.getReachableStatusList()) {
            String key = reachableStatusEntity.getCpId() + reachableStatusEntity.getOppositeType()
                + reachableStatusEntity.getOppositeId();
            String keyReverse = reachableStatusEntity.getOppositeId() + reachableStatusEntity.getOppositeType()
                + reachableStatusEntity.getCpId();

            if (reachableMap.containsKey(key)) {

              if (SliceUnitReachableStatus.REACHABLE.equals(reachableStatusEntity.getReachableStatusEnum())) {
                reachableMap.get(key).setReachableStatusEnum(SliceUnitReachableStatus.REACHABLE);
              }
            } else if (reachableMap.containsKey(keyReverse)) {

              if (SliceUnitReachableStatus.REACHABLE.equals(reachableStatusEntity.getReachableStatusEnum())) {
                reachableMap.get(keyReverse).setReachableStatusEnum(SliceUnitReachableStatus.REACHABLE);
              }
            } else {

              reachableMap.put(key, reachableStatusEntity);
            }
          }

          sliceEntity.setFailureStatusEnum(FailureStatus.UP);
          for (FailureStatusReachableStatusFailureEntity reachableStatusEntity : reachableMap.values()) {

            if (SliceUnitReachableStatus.UNREACHABLE.equals(reachableStatusEntity.getReachableStatusEnum())) {
              sliceEntity.setFailureStatusEnum(FailureStatus.DOWN);
              break;
            }
          }
          sliceEntity.setReachableStatusList(new ArrayList<>(reachableMap.values()));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void putCpReachability(Map<CpData, SliceUnitReachableStatus> originalMap,
      Map<CpData, SliceUnitReachableStatus> targetMap) {
    try {
      logger.methodStart();
      for (Entry<CpData, SliceUnitReachableStatus> entry : targetMap.entrySet()) {

        if (originalMap.containsKey(entry.getKey())) {

          if (SliceUnitReachableStatus.REACHABLE.equals(entry.getValue())) {
            originalMap.put(entry.getKey(), SliceUnitReachableStatus.REACHABLE);
          }
        } else {

          originalMap.put(entry.getKey(), entry.getValue());
        }
      }
    } finally {
      logger.methodEnd();
    }
  }
}
