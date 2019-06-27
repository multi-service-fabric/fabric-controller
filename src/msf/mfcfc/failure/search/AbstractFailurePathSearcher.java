
package msf.mfcfc.failure.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SliceUnitReachableOppositeType;
import msf.mfcfc.common.constant.SliceUnitReachableStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.status.data.entity.FailureStatusReachableStatusFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkReachableStatusEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;

/**
 * Base class for the failure path identification process in the failure
 * management function
 *
 */
public abstract class AbstractFailurePathSearcher {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractFailurePathSearcher.class);

  protected Set<DiNode> diNodeSet;

  protected Set<DiEdge> diEdgeSet;

  protected Set<DiNodePair> diNodePairSet;

  protected GraphBuilder<DiNode, DiEdge> builder;

  protected HipsterDirectedGraph<DiNode, DiEdge> hipsterDirectedGraph = null;

  protected FailureStatusSliceUnitEntity failureStatusSliceUnitEntity = new FailureStatusSliceUnitEntity();

  protected Map<SliceType, Map<String, FailureStatusSliceFailureEntity>> sliceMap = new HashMap<>();

  protected FailureStatusSliceClusterLinkFailureEntity clusterLink = null;

  protected void init() {

    diNodeSet = new TreeSet<>();
    diEdgeSet = new HashSet<>();

    diNodePairSet = new TreeSet<>();

    builder = GraphBuilder.create();
    hipsterDirectedGraph = null;

    failureStatusSliceUnitEntity = new FailureStatusSliceUnitEntity();

    sliceMap = new HashMap<>();

    clusterLink = null;
  }

  protected void createGraph() {
    hipsterDirectedGraph = builder.createDirectedGraph();
  }

  protected void calculatePath(DiNodePair diNodePair) {
    try {
      logger.methodStart(new String[] { "diNodePair" }, new Object[] { diNodePair });
      SearchProblem<DiEdge, DiNode, WeightedNode<DiEdge, DiNode, Double>> problem = GraphSearchProblem
          .startingFrom(diNodePair.getFrom()).in(hipsterDirectedGraph).takeCostsFromEdges().build();
      Algorithm<DiEdge, DiNode, WeightedNode<DiEdge, DiNode, Double>>.SearchResult searchResult = Hipster
          .createDijkstra(problem).search(diNodePair.getTo());
      List<DiNode> diNodeList = Algorithm.recoverStatePath(searchResult.getGoalNode());

      if (diNodeList.contains(diNodePair.getTo())) {
        diNodePair.setReachable(true);
      }

      String shortestPath = "";
      for (DiNode diNode : diNodeList) {

        if (!shortestPath.equals("")) {
          shortestPath += "-";
        }
        shortestPath += diNode.getStrForNodeSpecify();
      }
      diNodePair.setShortestPath(shortestPath);
    } finally {
      logger.methodEnd();
    }
  }

  protected void createDiEdge(DiNode from, DiNode to) {
    DiEdge edge = new DiEdge(from, to);
    diEdgeSet.add(edge);
    builder.connect(from).to(to).withEdge(edge);

  }

  protected void createDiEdge(DiNode from, DiNode to, double linkCost) {
    DiEdge edge = new DiEdge(from, to, linkCost);
    diEdgeSet.add(edge);
    builder.connect(from).to(to).withEdge(edge);

  }

  protected void addFailureStatusReachableStatusFailureEntity(SliceType sliceType, String sliceId, String cpId,
      SliceUnitReachableOppositeType oppositeType, String oppositeId, SliceUnitReachableStatus reachableStatus) {
    FailureStatusSliceFailureEntity sliceFailureEntity = null;

    Map<String, FailureStatusSliceFailureEntity> sliceIdMap = sliceMap.computeIfAbsent(sliceType,
        type -> new HashMap<>());

    sliceFailureEntity = sliceIdMap.computeIfAbsent(sliceId, id -> new FailureStatusSliceFailureEntity(id,
        sliceType.getMessage(), new TreeSet<String>(), FailureStatus.UP.getMessage(), new ArrayList<>()));

    sliceFailureEntity.getReachableStatusList().add(new FailureStatusReachableStatusFailureEntity(cpId,
        oppositeType.getMessage(), oppositeId, reachableStatus.getMessage()));

    sliceFailureEntity.getCpIdSet().add(cpId);

    if (oppositeType.equals(SliceUnitReachableOppositeType.CP)) {

      sliceFailureEntity.getCpIdSet().add(oppositeId);

      if (SliceUnitReachableStatus.UNREACHABLE.equals(reachableStatus)) {
        sliceFailureEntity.setFailureStatusEnum(FailureStatus.DOWN);
      }
    }

  }

  protected void addFailureStatusSliceClusterLinkReachableStatusEntity(String clusterLinkIfId,
      String oppositeClusterLinkIfId, SliceUnitReachableStatus reachableStatus) {
    if (clusterLink == null) {
      clusterLink = new FailureStatusSliceClusterLinkFailureEntity();
    }
    if (clusterLink.getReachableStatusList() == null) {
      clusterLink.setReachableStatusList(new ArrayList<>());
    }
    clusterLink.getReachableStatusList().add(new FailureStatusSliceClusterLinkReachableStatusEntity(clusterLinkIfId,
        oppositeClusterLinkIfId, reachableStatus.getMessage()));
  }

  protected abstract void createAllNodes(SessionWrapper sessionWrapper) throws MsfException;

  protected abstract void createAllEdges() throws MsfException;

  protected abstract void createStartGoalNodePairs() throws MsfException;

  protected abstract void createAllReachableStatuses() throws MsfException;
}
