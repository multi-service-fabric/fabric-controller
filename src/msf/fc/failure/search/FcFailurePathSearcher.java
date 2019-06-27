
package msf.fc.failure.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import msf.mfcfc.common.util.Tuple;
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

      diNodeSet.stream().flatMap(x -> diNodeSet.stream().filter(y -> y.compareTo(x) >= 0).map(y -> new Tuple<>(x, y)))

          .forEach(t -> {
            DiNode diNode = t.getElement1();
            DiNode diNode2 = t.getElement2();
            boolean isDiNodeLeaf = (diNode instanceof FcLeafDiNode);
            boolean isDiNodeLeaf2 = (diNode2 instanceof FcLeafDiNode);

            if (isDiNodeLeaf && isDiNodeLeaf2) {

              DiNodePair diNodePair = new DiNodePair(diNode, diNode2);

              calculatePath(diNodePair);
              diNodePairSet.add(diNodePair);
            }
          });

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
        boolean sameNode = (fromNode.compareTo(toNode) == 0);

        Map<String, List<SliceUnitFailureEndPointData>> fromL2CpFailures = fromNode.getL2CpEndPointDataMap();
        Map<String, List<SliceUnitFailureEndPointData>> fromL3CpFailures = fromNode.getL3CpEndPointDataMap();
        List<SliceUnitFailureEndPointData> fromClusterIfFailures = fromNode.getClusterIfEndPointDataList();

        Map<String, List<SliceUnitFailureEndPointData>> toL2CpFailures = toNode.getL2CpEndPointDataMap();
        Map<String, List<SliceUnitFailureEndPointData>> toL3CpFailures = toNode.getL3CpEndPointDataMap();
        List<SliceUnitFailureEndPointData> toClusterIfFailures = toNode.getClusterIfEndPointDataList();

        Map<String, List<Tuple<List<SliceUnitFailureEndPointData>, List<List<SliceUnitFailureEndPointData>>>>> l2Map = new java.util.HashMap<>();
        Map<String, List<Tuple<List<SliceUnitFailureEndPointData>, List<List<SliceUnitFailureEndPointData>>>>> l3Map = new java.util.HashMap<>();
        List<Tuple<List<SliceUnitFailureEndPointData>, List<List<SliceUnitFailureEndPointData>>>> clusterList = Arrays
            .asList(new Tuple<>(fromClusterIfFailures, Arrays.asList(toClusterIfFailures)));

        fromL2CpFailures.forEach((sliceId, cps) -> {
          List<List<SliceUnitFailureEndPointData>> toList = new ArrayList<>();
          if (toL2CpFailures.containsKey(sliceId)) {
            toList.add(toL2CpFailures.get(sliceId));
          }
          toList.add(toClusterIfFailures);
          l2Map.computeIfAbsent(sliceId, id -> new ArrayList<>()).add(new Tuple<>(cps, toList));
        });

        fromL3CpFailures.forEach((sliceId, cps) -> {
          List<List<SliceUnitFailureEndPointData>> toList = new ArrayList<>();
          if (toL3CpFailures.containsKey(sliceId)) {
            toList.add(toL3CpFailures.get(sliceId));
          }
          toList.add(toClusterIfFailures);
          l3Map.computeIfAbsent(sliceId, id -> new ArrayList<>()).add(new Tuple<>(cps, toList));
        });

        if (!sameNode) {

          toL2CpFailures.forEach((sliceId, cps) -> {
            List<List<SliceUnitFailureEndPointData>> toList = Arrays.asList(fromClusterIfFailures);
            l2Map.computeIfAbsent(sliceId, id -> new ArrayList<>()).add(new Tuple<>(cps, toList));
          });

          toL3CpFailures.forEach((sliceId, cps) -> {
            List<List<SliceUnitFailureEndPointData>> toList = Arrays.asList(fromClusterIfFailures);
            l3Map.computeIfAbsent(sliceId, id -> new ArrayList<>()).add(new Tuple<>(cps, toList));
          });
        }

        Stream.of(l2Map.values(), l3Map.values(), Arrays.asList(clusterList)).forEach(tupleListList -> {

          tupleListList.stream()

              .flatMap(
                  tupleList -> tupleList.stream()
                      .flatMap(
                          tuple -> tuple.getElement1().stream()
                              .flatMap(
                                  fromEndPoint -> tuple.getElement2().stream()
                                      .flatMap(toEndPointList -> toEndPointList.stream()
                                          .map(toEndPoint -> new Tuple<>(fromEndPoint, toEndPoint))))))
              .forEach(fromTo -> {
                SliceUnitFailureEndPointData fromEndPoint = fromTo.getElement1();
                SliceUnitFailureEndPointData toEndPoint = fromTo.getElement2();

                if (sameNode && fromEndPoint.compareTo(toEndPoint) >= 0) {
                  return;
                }

                createFailureStatusEntity(fromEndPoint, toEndPoint, diNodePair.isReachable());
              });
        });
      }

      failureStatusSliceUnitEntity
          .setSliceList(sliceMap.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList()));
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

      addFailureStatusReachableStatusFailureEntity(fromEndpoint.getSliceType(), fromEndpoint.getSliceId(),
          fromEndpoint.getEndPointId(), SliceUnitReachableOppositeType.CP, toEndpoint.getEndPointId(), reachableStatus);
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
