
package msf.fc.failure.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceUnitReachableOppositeType;
import msf.mfcfc.common.constant.SliceUnitReachableStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusIfEntity;
import msf.mfcfc.failure.search.AbstractFailurePathSearcher;
import msf.mfcfc.failure.search.DiNode;
import msf.mfcfc.failure.search.DiNodePair;
import msf.mfcfc.failure.search.SliceUnitFailureEndPointData;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;

/**
 * Implementation class for the failure path identification process in the FC
 * failure management function.
 *
 */
public class FcFailurePathSearcher extends AbstractFailurePathSearcher {

  private static final MsfLogger logger = MsfLogger.getInstance(FcFailurePathSearcher.class);

  private Map<LogicalIfStatusIfEntity, FcClusterLinkIf> clusterLinkMap;
  private Map<LogicalIfStatusIfEntity, FcL2Cp> l2CpMap;
  private Map<LogicalIfStatusIfEntity, FcL3Cp> l3CpMap;
  private Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> internalLinkPairMap;

  @Override
  protected void createAllNodes(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao fcNodeDao = new FcNodeDao();

      List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

      int topologyNodeId = 1;
      for (FcNode fcNode : fcNodeList) {
        switch (fcNode.getNodeTypeEnum()) {
          case LEAF:
            FcLeafDiNode fcLeafDiNode = new FcLeafDiNode(topologyNodeId, fcNode);

            for (LogicalIfStatusIfEntity entity : l2CpMap.keySet()) {
              if (fcNode.getEcNodeId().toString().equals(entity.getNodeId())) {
                fcLeafDiNode.getL2CpMap().put(l2CpMap.get(entity), entity.getStatusEnum());
              }
            }

            for (LogicalIfStatusIfEntity entity : l3CpMap.keySet()) {
              if (fcNode.getEcNodeId().toString().equals(entity.getNodeId())) {
                fcLeafDiNode.getL3CpMap().put(l3CpMap.get(entity), entity.getStatusEnum());
              }
            }

            setOppositeNodeFailureStatus(sessionWrapper, fcNodeDao, fcNode, fcLeafDiNode);

            if (fcLeafDiNode.isBLeaf()) {
              for (LogicalIfStatusIfEntity entity : clusterLinkMap.keySet()) {
                FcClusterLinkIf fcClusterLinkIf = clusterLinkMap.get(entity);

                if (checkClusterLinkIfOnNode(fcNode, fcClusterLinkIf)) {
                  fcLeafDiNode.getClusterLinkIfMap().put(clusterLinkMap.get(entity), entity.getStatusEnum());
                }
              }
            }

            diNodeSet.add(fcLeafDiNode);

            topologyNodeId++;
            break;
          case SPINE:
            FcSpineDiNode fcSpineDiNode = new FcSpineDiNode(topologyNodeId, fcNode);
            setOppositeNodeFailureStatus(sessionWrapper, fcNodeDao, fcNode, fcSpineDiNode);

            diNodeSet.add(fcSpineDiNode);

            topologyNodeId++;
            break;
          default:

            logger.debug("not target. nodetype = {0}", fcNode.getNodeTypeEnum());
            break;
        }
      }

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
          FcNodeDiNode fcNodeDiNode = (FcNodeDiNode) diNode;
          FcNodeDiNode fcNodeDiNode2 = (FcNodeDiNode) diNode2;

          if (fcNodeDiNode.getOppositeNodeMap().containsKey(fcNodeDiNode2.getFcNode())
              && fcNodeDiNode2.getOppositeNodeMap().containsKey(fcNodeDiNode.getFcNode())) {

            if (fcNodeDiNode.getOppositeNodeMap().get(fcNodeDiNode2.getFcNode()).equals(FailureStatus.UP)
                && fcNodeDiNode2.getOppositeNodeMap().get(fcNodeDiNode.getFcNode()).equals(FailureStatus.UP)) {

              createDiEdge(fcNodeDiNode, fcNodeDiNode2);
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
        for (DiNode diNode2 : diNodeSet) {
          boolean isDiNodeLeaf = (diNode instanceof FcLeafDiNode);
          boolean isDiNodeLeaf2 = (diNode2 instanceof FcLeafDiNode);

          if (isDiNodeLeaf && isDiNodeLeaf2) {

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
      }
      logger.debug("created node pairs = " + diNodePairSet);

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
   *          IF information of inter-cluster link IF notified by failure
   *          notification
   * @param l2CpMap
   *          IF information of the L2CP notified by failure notification
   * @param l3CpMap
   *          IF information of the L3CP notified by failure notification
   * @param internalLinkPairMap
   *          Both endpoints information of intra-cluster link IF
   * @return failure notification information for slice by slice
   * @throws MsfException
   *           When a DB access error occurs
   */
  public FailureStatusSliceUnitEntity searchFailurePath(SessionWrapper sessionWrapper,
      Map<LogicalIfStatusIfEntity, FcClusterLinkIf> clusterLinkMap, Map<LogicalIfStatusIfEntity, FcL2Cp> l2CpMap,
      Map<LogicalIfStatusIfEntity, FcL3Cp> l3CpMap,
      Map<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> internalLinkPairMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkMap", "l2CpMap", "l3CpMap", "internalLinkPairMap" },
          new Object[] { clusterLinkMap, l2CpMap, l3CpMap, internalLinkPairMap });
      this.clusterLinkMap = clusterLinkMap;
      this.l2CpMap = l2CpMap;
      this.l3CpMap = l3CpMap;
      this.internalLinkPairMap = internalLinkPairMap;

      init();

      createAllNodes(sessionWrapper);

      createAllEdges();

      createGraph();

      createStartGoalNodePairs();

      createAllReachableStatuses();

      logger.debug("failureStatusSliceUnitEntity : " + failureStatusSliceUnitEntity);
      return failureStatusSliceUnitEntity;
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      throw exp;
    } finally {
      logger.methodEnd();
    }
  }

  private FailureStatus checkFailureStatusFromInternalLinkPair(
      Map.Entry<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> entry) {

    if (FailureStatus.DOWN.equals(entry.getKey().getStatusEnum())
        || FailureStatus.DOWN.equals(entry.getValue().getStatusEnum())) {
      return FailureStatus.DOWN;
    } else {
      return FailureStatus.UP;
    }
  }

  private void setOppositeNodeFailureStatus(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, FcNode fcNode,
      FcNodeDiNode fcNodeDiNode) throws MsfException {
    for (Map.Entry<LogicalIfStatusIfEntity, LogicalIfStatusIfEntity> entry : internalLinkPairMap.entrySet()) {

      if (fcNode.getEcNodeId().toString().equals(entry.getKey().getNodeId())) {
        FcNode oppositeFcNode = fcNodeDao.readByEcNodeId(sessionWrapper, Integer.valueOf(entry.getValue().getNodeId()));
        fcNodeDiNode.getOppositeNodeMap().put(oppositeFcNode, checkFailureStatusFromInternalLinkPair(entry));
      } else if (fcNode.getEcNodeId().toString().equals(entry.getValue().getNodeId())) {
        FcNode oppositeFcNode = fcNodeDao.readByEcNodeId(sessionWrapper, Integer.valueOf(entry.getKey().getNodeId()));
        fcNodeDiNode.getOppositeNodeMap().put(oppositeFcNode, checkFailureStatusFromInternalLinkPair(entry));
      }
    }
  }

  @Override
  protected void createAllReachableStatuses() throws MsfException {
    try {
      logger.methodStart();

      for (DiNodePair diNodePair : diNodePairSet) {
        FcLeafDiNode fromNode = (FcLeafDiNode) diNodePair.getFrom();
        FcLeafDiNode toNode = (FcLeafDiNode) diNodePair.getTo();

        List<Map<SliceUnitFailureEndPointData, SliceUnitFailureEndPointData>> createdPairList = new ArrayList<>();

        for (SliceUnitFailureEndPointData fromEndpoint : fromNode.getAllEndPointDataList()) {

          for (SliceUnitFailureEndPointData toEndpoint : toNode.getAllEndPointDataList()) {

            if (fromEndpoint.equals(toEndpoint)) {
              continue;
            }

            boolean isSkip = false;
            check: {
              for (Map<SliceUnitFailureEndPointData, SliceUnitFailureEndPointData> map : createdPairList) {
                for (SliceUnitFailureEndPointData key : map.keySet()) {
                  if (key.equals(toEndpoint) && map.get(key).equals(fromEndpoint)) {
                    isSkip = true;
                    break check;
                  }
                }
              }
            }
            if (isSkip) {
              continue;
            }

            createFailureStatusEntity(fromEndpoint, toEndpoint, diNodePair.isReachable());
            Map<SliceUnitFailureEndPointData, SliceUnitFailureEndPointData> pairMap = new HashMap<>();
            pairMap.put(fromEndpoint, toEndpoint);
            createdPairList.add(pairMap);

          }
        }
      }

      failureStatusSliceUnitEntity.setSliceList(sliceList);
      failureStatusSliceUnitEntity.setClusterLink(clusterLink);

    } finally {
      logger.methodEnd();
    }
  }

  private void createFailureStatusEntity(SliceUnitFailureEndPointData fromEndpoint,
      SliceUnitFailureEndPointData toEndpoint, boolean existRoute) {

    SliceUnitReachableStatus reachableStatus = SliceUnitReachableStatus.REACHABLE;

    if (!existRoute || !fromEndpoint.getFailureStatus().equals(FailureStatus.UP)
        || !toEndpoint.getFailureStatus().equals(FailureStatus.UP)) {
      reachableStatus = SliceUnitReachableStatus.UNREACHABLE;
    }

    if (fromEndpoint.isCp() && toEndpoint.isCp()) {

      if (fromEndpoint.getSliceType().equals(toEndpoint.getSliceType())
          && fromEndpoint.getSliceId().equals(toEndpoint.getSliceId())) {
        addFailureStatusReachableStatusFailureEntity(fromEndpoint.getSliceType(), fromEndpoint.getSliceId(),
            fromEndpoint.getEndPointId(), SliceUnitReachableOppositeType.CP, toEndpoint.getEndPointId(),
            reachableStatus);
      }
    } else if (fromEndpoint.isCp() && !toEndpoint.isCp()) {

      addFailureStatusReachableStatusFailureEntity(fromEndpoint.getSliceType(), fromEndpoint.getSliceId(),
          fromEndpoint.getEndPointId(), SliceUnitReachableOppositeType.CLUSTER_LINK_IF, toEndpoint.getEndPointId(),
          reachableStatus);
    } else if (!fromEndpoint.isCp() && toEndpoint.isCp()) {

      addFailureStatusReachableStatusFailureEntity(toEndpoint.getSliceType(), toEndpoint.getSliceId(),
          toEndpoint.getEndPointId(), SliceUnitReachableOppositeType.CLUSTER_LINK_IF, fromEndpoint.getEndPointId(),
          reachableStatus);
    } else {

      addFailureStatusSliceClusterLinkReachableStatusEntity(fromEndpoint.getEndPointId(), toEndpoint.getEndPointId(),
          reachableStatus);
    }
  }

  private boolean checkClusterLinkIfOnNode(FcNode fcNode, FcClusterLinkIf fcClusterLinkIf) throws MsfException {
    try {
      logger.methodStart();
      if (fcClusterLinkIf.getPhysicalIf() != null) {
        return fcNode.getNodeInfoId().equals(fcClusterLinkIf.getPhysicalIf().getNode().getNodeInfoId());
      } else if (fcClusterLinkIf.getLagIf() != null) {
        return fcNode.getNodeInfoId().equals(fcClusterLinkIf.getLagIf().getNode().getNodeInfoId());
      } else if (fcClusterLinkIf.getBreakoutIf() != null) {
        return fcNode.getNodeInfoId().equals(fcClusterLinkIf.getBreakoutIf().getNode().getNodeInfoId());
      }
      return false;
    } finally {
      logger.methodEnd();
    }
  }
}
