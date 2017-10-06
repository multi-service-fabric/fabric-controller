package msf.fc.traffic.tm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.HibernateException;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.traffic.data.TrafficCommonData;

public final class TrafficVirtualTopology {

  private static final TrafficVirtualTopology trafficVirtualTopology = new TrafficVirtualTopology();

  public static final String TOMOGEAVITY = "tomogravity";

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficVirtualTopology.class);

  private Set<DiEdge> diEdgeSet;


  private GraphBuilder<DiNode, DiEdge> builder;

  private TrafficCommonData trafficCommonData;

  TrafficVirtualTopology() {
    init();
    trafficCommonData = TrafficCommonData.getInstance();
  }

  public static TrafficVirtualTopology getInstance() {
    return trafficVirtualTopology;
  }

  protected void init() {
    diNodeSet = new TreeSet<>();
    diEdgeSet = new HashSet<>();
    vertexSet = new TreeSet<>();

    builder = GraphBuilder.create();
  }

  protected HipsterDirectedGraph<DiNode, DiEdge> getGraph() {
    return builder.createDirectedGraph();
  }

  protected void setDiNodeSet(Set<DiNode> diNodeSet) {
    this.diNodeSet = diNodeSet;
  }

  protected Set<DiNode> getDiNodeSet() {
    return diNodeSet;
  }

  protected Set<DiEdge> getDiEdgeSet() {
    return diEdgeSet;
  }

  protected Set<DiNode> getVertexSet() {
    return vertexSet;
  }

  protected void setAllPair(Set<DiNodePair> allPair) {
    this.allPair = allPair;
  }

  protected Set<DiNodePair> getAllPair() {
    return allPair;
  }

  protected void setGroundPair(Set<DiNodePair> groundPair) {
    this.groundPair = groundPair;
  }

  protected Set<DiNodePair> getGroundPair() {
    return groundPair;
  }

  protected void setInOutPair(List<DiNodePair> inOutPair) {
    this.inOutPair = inOutPair;
  }

  protected List<DiNodePair> getInOutPair() {
    return inOutPair;
  }

  public synchronized void create() throws MsfException, InterruptedException {
    logger.methodStart();

    SessionWrapper sessionWrapper = new SessionWrapper();

    try {
      init();
      sessionWrapper.openSession();

      List<Node> nodeList = new LinkedList<>();
      for (SwCluster cluster : new SwClusterDao().readList(sessionWrapper)) {
        nodeList.addAll(new NodeDao().readList(sessionWrapper, cluster.getSwClusterId()));
      }

      for (Node node : nodeList) {
        node.getEquipment().getId();
        node.toString();
        for (LagIf lagIf : node.getLagIfs()) {
          lagIf.getInternalLinkIf();
        }
      }

      List<DiNode> diNodelist = create(sessionWrapper, nodeList);

      setNumber(diNodelist);

      createEdge();

    } catch (MsfException exp) {
      logger.debug("Create VirtualTopology Exception.", exp);
      throw exp;
    } catch (IllegalArgumentException iaexp) {
      logger.warn("VirtualTopology IllegalArgumentException.");
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "Node information Nothing.");
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private List<DiNode> create(SessionWrapper sessionWrapper, List<Node> nodeList)
      throws MsfException, IllegalArgumentException, InterruptedException {
    try {
      logger.methodStart();

      if (nodeList.isEmpty()) {
        logger.warn("Create VirtualTopology error. Reason : nodeList is empty.");
        throw new IllegalArgumentException();
      }

      List<DiNode> retdNodeList = new LinkedList<DiNode>();

      List<Node> nodeListlo = new LinkedList<>(nodeList);

      Iterator<Node> ite = nodeListlo.iterator();
      while (ite.hasNext()) {
        Node target = ite.next();
        switch (target.getNodeTypeEnum()) {
            createL2L3Node(sessionWrapper, target, retdNodeList);
            break;

            continue;

          default:
            logger.warn("Create VirtualTopology error. Reason : Illeagal NodeType.");
            throw new IllegalArgumentException();
        }
      }

      while (true) {
        if (nodeListlo.isEmpty()) {
          break;
        }

        Node node = nodeListlo.remove(0);

        List<Node> commonList = new LinkedList<>();
        commonList.add(node);

        String sumNodeId = getSumNodeId(node);

        Iterator<Node> ite2 = nodeListlo.iterator();
        while (ite2.hasNext()) {
          Node target = ite2.next();

          String targetSumNodeId = getSumNodeId(target);
          if (targetSumNodeId.equals("")) {
            continue;
          }
          if (sumNodeId.equals(targetSumNodeId)) {
            commonList.add(target);
            ite2.remove();
          }
        }

        retdNodeList.add(new CommonSpineDiNode(commonList, DiNodeType.CommonSpine));
        retdNodeList.add(new CommonSpineDiNode(commonList, DiNodeType.INOUT));
      }
      return retdNodeList;
    } finally {
      logger.methodEnd();
    }
  }

  private void createL2L3Node(SessionWrapper sessionWrapper, Node node, List<DiNode> diNodeList)
      throws MsfException, IllegalArgumentException {
    try {
      logger.methodStart();
      List<PhysicalIf> physicalIfList = node.getPhysicalIfs();

      switch (node.getVpnTypeEnum()) {
          diNodeList.add(new DiNode(node, DiNodeType.L2Leaf));
          diNodeList.add(new DiNode(node, DiNodeType.INOUT));
          for (PhysicalIf physicalIf : physicalIfList) {
            EdgePoint edgePoint = getEdgePoint(sessionWrapper, physicalIf);
              edgePoint.toString();
              edgePoint.getSwCluster();

              boolean sameEdgePoint = false;
              for (DiNode tmp : diNodeList) {
                if ((tmp.getEdgePoint() != null) && (tmp.getEdgePoint().getId().equals(edgePoint.getId()))) {
                  sameEdgePoint = true;
                  break;
                }
              }
              if (!sameEdgePoint) {
                diNodeList.add(new DiNode(node, edgePoint));
                diNodeList.add(new DiNode(node, edgePoint, DiNodeType.INOUT));
              }
            }
          }
          break;

          diNodeList.add(new DiNode(node, DiNodeType.L3Leaf));
          diNodeList.add(new DiNode(node, DiNodeType.INOUT));
          for (PhysicalIf physicalIf : physicalIfList) {
            EdgePoint edgePoint = getEdgePoint(sessionWrapper, physicalIf);
              edgePoint.getSwCluster();
              for (L3Cp l3cp : edgePoint.getL3Cps()) {
                if (ActiveStatus.INACTIVE.equals(l3cp.getStatusEnum())) {
                  continue;
                }

                boolean sameL3cp = false;
                for (DiNode tmpL3cp : diNodeList) {
                  if ((tmpL3cp.getL3Cp() != null) && tmpL3cp.getL3Cp().getId().equals(l3cp.getId())) {
                    sameL3cp = true;
                    break;
                  }
                }
                if (!sameL3cp) {
                  l3cp.toString();
                  l3cp.getL3Slice().getSliceId();
                  l3cp.getEdgePoint();
                  diNodeList.add(new DiNode(node, l3cp));
                  diNodeList.add(new DiNode(node, l3cp, DiNodeType.INOUT));
                }
              }
            }
          }
          break;

        default:
          throw new IllegalArgumentException();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePoint getEdgePoint(SessionWrapper sessionWrapper, PhysicalIf physicalIf) throws MsfException {
    logger.methodStart();

    try {
      EdgePointDao edgePointDao = new EdgePointDao();

      EdgePoint edgePoint = null;

      LagConstruction lagConstruction = physicalIf.getLagConstruction();
        LagIf lagIf = lagConstruction.getLagIf();
        edgePoint = edgePointDao.readBylagIfInfoId(sessionWrapper, lagIf.getLagIfInfoId());
        edgePoint = edgePointDao.readByPhysicalIfInfoId(sessionWrapper, physicalIf.getPhysicalIfInfoId());
      }

      if (edgePoint != null) {
        edgePoint.getL2Cps();
        edgePoint.getL3Cps();
      }
      return edgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  protected void setNumber(List<DiNode> diNodeList) throws InterruptedException {
    try {
      logger.methodStart();
      diNodeSet = new TreeSet<DiNode>();
      vertexSet = new TreeSet<DiNode>();

      List<DiNode> inoutList = new LinkedList<DiNode>();

      int no = 0;

      for (DiNode diNode : diNodeList) {
        switch (diNode.nodeType) {
          case INOUT:
            inoutList.add(diNode);
            break;
          default:
            diNode.setNodeNo(no++);
            diNodeSet.add(diNode);
            break;
        }
      }
      vertexSet.addAll(diNodeSet);

      for (DiNode inout : inoutList) {
        inout.setNodeNo(no++);
        diNodeSet.add(inout);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void createEdge() throws InterruptedException, MsfException {
    for (DiNode diNode : diNodeSet) {
      switch (diNode.nodeType) {
        case CommonSpine:
          createCommonSpineEdge((CommonSpineDiNode) diNode);
          break;
        case L2Leaf:
          createL2LeafEdge(diNode);
          break;
        case L3Leaf:
          createL3LeafEdge(diNode);
          break;
        default:
          break;
      }
    }
  }

  protected void createDiEdge(DiNode from, DiNode to) {
    DiEdge edge = new DiEdge(from, to);
    diEdgeSet.add(edge);
    builder.connect(from).to(to).withEdge(edge);
  }

  private void createCommonSpineEdge(CommonSpineDiNode commonSpineDiNode) {
    DiNode inout = find(commonSpineDiNode);
    if (inout != null) {
      createDiEdge(commonSpineDiNode, inout);
      createDiEdge(inout, commonSpineDiNode);
    }
  }

  private void createL2LeafEdge(DiNode diNode) throws MsfException {
    try {
      logger.methodStart();
      List<DiNode> l2EpList = new LinkedList<DiNode>();
      for (DiNode target : diNodeSet) {
        if ((DiNodeType.L2EP.equals(target.nodeType)) && (diNode.node.equals(target.node))) {
          createDiEdge(target, diNode);
          createDiEdge(diNode, target);
          l2EpList.add(target);
        }
      }

      for (DiNode target : diNodeSet) {
        if (DiNodeType.CommonSpine.equals(target.nodeType)) {
          if (((CommonSpineDiNode) target).spineNodeList.size() > 0) {
            Node spine = ((CommonSpineDiNode) target).spineNodeList.get(0);
            for (PhysicalIf physicalIf : spine.getPhysicalIfs()) {
              if (physicalIf.getOppositeNode() != null) {
                Node oppositNode = physicalIf.getOppositeNode();
                if (oppositNode.equals(diNode.node)) {
                  createDiEdge(diNode, target);
                  createDiEdge(target, diNode);
                }
              }
            }
          }
        }
      }

      for (DiNode l2ep : l2EpList) {
        for (DiNode target : diNodeSet) {
          if ((DiNodeType.INOUT.equals(target.nodeType)) && (l2ep.node.equals(target.node))
              && (l2ep.getEdgePoint().equals(target.getEdgePoint()))) {
            createDiEdge(l2ep, target);
            createDiEdge(target, l2ep);
          }
        }
      }

      for (DiNode target : diNodeSet) {
        if ((DiNodeType.INOUT.equals(target.nodeType)) && (!(target instanceof CommonSpineDiNode))
            && (target.node.equals(diNode.node)) && (target.getEdgePoint() == null)) {
          createDiEdge(diNode, target);
          createDiEdge(target, diNode);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void createL3LeafEdge(DiNode diNode) throws MsfException {
    try {
      logger.methodStart();
      List<DiNode> l3CpList = new LinkedList<DiNode>();

      for (DiNode target : diNodeSet) {
        if ((DiNodeType.L3CP.equals(target.nodeType)) && (diNode.node.equals(target.node))
            && (diNode.getL3Cp() == null)) {
          createDiEdge(target, diNode);
          createDiEdge(diNode, target);
          l3CpList.add(target);
        }
      }

      for (DiNode target : diNodeSet) {
        if (DiNodeType.CommonSpine.equals(target.nodeType)) {
          if (((CommonSpineDiNode) target).spineNodeList.size() > 0) {
            Node spine = ((CommonSpineDiNode) target).spineNodeList.get(0);
            for (PhysicalIf physicalIf : spine.getPhysicalIfs()) {
              if (physicalIf.getOppositeNode() != null) {
                Node oppositNode = physicalIf.getOppositeNode();
                if (oppositNode.equals(diNode.node)) {
                  createDiEdge(diNode, target);
                  createDiEdge(target, diNode);
                }
              }
            }
          }
        }
      }

      for (DiNode l3cp : l3CpList) {
        for (DiNode target : diNodeSet) {
          if ((DiNodeType.INOUT.equals(target.nodeType)) && (l3cp.node.equals(target.node))
              && (l3cp.getL3Cp().equals(target.getL3Cp()))) {
            createDiEdge(l3cp, target);
            createDiEdge(target, l3cp);
          }
        }
      }

      for (DiNode target : diNodeSet) {
        if ((DiNodeType.INOUT.equals(target.nodeType)) && (!(target instanceof CommonSpineDiNode))
            && (target.node.equals(diNode.node)) && (target.getL3Cp() == null)) {
          createDiEdge(diNode, target);
          createDiEdge(target, diNode);
        }
      }
    } finally {
      logger.methodEnd();
    }

  }

  protected DiNode find(int nodeInfoId) {
    for (DiNode diNode : diNodeSet) {
      if ((diNode.node != null) && (diNode.node.getNodeInfoId() == nodeInfoId)) {
        return diNode;
      }
    }
    return null;
  }

  protected DiNode find(CommonSpineDiNode commonSpineDiNode) {
    for (DiNode diNode : diNodeSet) {
      if ((diNode.nodeType == DiNodeType.INOUT) && (diNode instanceof CommonSpineDiNode)
          && ((CommonSpineDiNode) diNode).spineNodeList.equals(commonSpineDiNode.spineNodeList)) {
        return diNode;
      }
    }
    return null;
  }

  protected enum DiNodeType {
    CommonSpine(1),
    L3Leaf(2),
    L3CP(3),
    L2Leaf(4),
    L2EP(5),
    INOUT(6);
    protected int type;

    private DiNodeType(int type) {
      this.type = type;
    }
  }

  protected class DiNode implements Comparable<DiNode> {
    private Node node;
    private DiNodeType nodeType;

    protected DiNode(DiNodeType nodeType) {
      this(null, null, null, nodeType, -1);
    }

    protected DiNode(DiNodeType nodeType, int nodeNo) {
      this(null, null, null, nodeType, nodeNo);
    }

    protected DiNode(Node node, DiNodeType nodeType) {
      this(node, null, null, nodeType, -1);
    }

    protected DiNode(Node node, EdgePoint edgePoint) {
      this(node, edgePoint, null, DiNodeType.L2EP, -1);
    }

    protected DiNode(Node node, EdgePoint edgePoint, DiNodeType nodeType) {
      this(node, edgePoint, null, nodeType, -1);
    }

    protected DiNode(Node node, L3Cp l3Cp) {
      this(node, null, l3Cp, DiNodeType.L3CP, -1);
    }

    protected DiNode(Node node, L3Cp l3Cp, DiNodeType nodeType) {
      this(node, null, l3Cp, nodeType, -1);
    }

    protected DiNode(Node node, EdgePoint edgePoint, L3Cp l3Cp, DiNodeType nodeType, int nodeNo) {
      this.node = node;
      this.edgePoint = edgePoint;
      this.l3Cp = l3Cp;
      this.nodeType = nodeType;
      this.nodeNo = nodeNo;
    }

    protected void setNodeNo(int nodeNo) {
      this.nodeNo = nodeNo;
    }

    protected String getNodeNoString() {
      return String.format("%08d", nodeNo);
    }

    @Override
    public int compareTo(DiNode target) {
      return nodeNo - target.nodeNo;
    }

    public Node getNode() {
      return node;
    }

    public void setNode(Node node) {
      this.node = node;
    }

    public EdgePoint getEdgePoint() {
      return edgePoint;
    }

    public void setEdgePoint(EdgePoint edgePoint) {
      this.edgePoint = edgePoint;
    }

    public L3Cp getL3Cp() {
      return l3Cp;
    }

    public void setL3Cp(L3Cp l3Cp) {
      this.l3Cp = l3Cp;
    }

    public DiNodeType getNodeType() {
      return nodeType;
    }

    public void setNodeType(DiNodeType nodeType) {
      this.nodeType = nodeType;
    }

    public int getNodeNo() {
      return nodeNo;
    }
  }

  protected class DiEdge extends Number implements Comparable<Double> {
    private static final long serialVersionUID = 1L;


    private DiNode from;
    private DiNode to;

    protected DiEdge(DiNode from, DiNode to) {
      this.from = from;
      this.to = to;
    }

    @Override
    public int intValue() {
      return weight.intValue();
    }

    @Override
    public long longValue() {
      return weight.longValue();
    }

    @Override
    public float floatValue() {
      return weight.floatValue();
    }

    @Override
    public double doubleValue() {
      return weight.doubleValue();
    }

    @Override
    public String toString() {
      return weight.toString();
    }

    @Override
    public int compareTo(Double target) {
      return weight.compareTo(target);
    }

    public Double getWeight() {
      return weight;
    }

    public void setWeight(Double weight) {
      this.weight = weight;
    }

    public DiNode getFrom() {
      return from;
    }

    public void setFrom(DiNode from) {
      this.from = from;
    }

    public DiNode getTo() {
      return to;
    }

    public void setTo(DiNode to) {
      this.to = to;
    }

  }

  protected class CommonSpineDiNode extends DiNode {
    private List<Node> spineNodeList;

    protected CommonSpineDiNode(List<Node> spineNodeList, DiNodeType nodeType) {
      this(spineNodeList, nodeType, -1);
    }

    protected CommonSpineDiNode(List<Node> spineNodeList, DiNodeType nodeType, int nodeNo) {
      super(nodeType, nodeNo);
      this.spineNodeList = spineNodeList;
    }

    protected List<Node> getSpineNodeList() {
      return spineNodeList;
    }
  }

  private String getSumNodeId(Node node) throws MsfException {
    String nodeSum = "";
    Set<Integer> oppositeNodeIdSet = new TreeSet<>();
    for (PhysicalIf physicalIf : node.getPhysicalIfs()) {
      if (physicalIf.getOppositeNode() != null) {
        oppositeNodeIdSet.add(physicalIf.getOppositeNode().getNodeId());
      }
    }
    for (Integer oppositeNodeId : oppositeNodeIdSet) {
      if (nodeSum.isEmpty()) {
      } else {
      }
    }
    return nodeSum;
  }

  protected class DiNodePair implements Comparable<DiNodePair> {



    private String compValue;

    private String nodePath;

    private Double trafficValue;

    protected DiNodePair(DiNode from, DiNode to) {
      this(from, to, null);
    }

    protected DiNodePair(DiNode from, DiNode to, Integer linkNo) {
      this.from = from;
      this.to = to;
      this.linkNo = linkNo;

      compValue = from.getNodeNoString() + "-" + to.getNodeNoString();
    }

    public Integer getLinkNo() {
      return linkNo;
    }

    protected void setLinkNo(Integer linkNo) {
      this.linkNo = linkNo;
    }

    protected void calcPath(HipsterDirectedGraph<DiNode, DiEdge> graph) {
      SearchProblem<DiEdge, DiNode, WeightedNode<DiEdge, DiNode, Double>> problem = GraphSearchProblem
          .startingFrom(from).in(graph).takeCostsFromEdges().build();
      Algorithm<DiEdge, DiNode, WeightedNode<DiEdge, DiNode, Double>>.SearchResult searchResult = Hipster
          .createDijkstra(problem).search(to);

      nodePath = "";
      List<DiNode> list = Algorithm.recoverStatePath(searchResult.getGoalNode());
      for (DiNode pathNode : list) {
        String pathNodeId = String.format("%08d", pathNode.nodeNo);
        nodePath += nodePath.equals("") ? pathNodeId : "-" + pathNodeId;
      }

      if (!nodePath.contains(String.format("%08d", to.nodeNo))) {
        nodePath = "";
      }

    }

    protected boolean isDirect() {
      if (nodePath == null) {
        logger.warn("Create VirtualTopology error. Reason : isDirect() nodePath is NULL.");
        throw new IllegalArgumentException();
      }
      if ("".equals(nodePath)) {
        return false;
      }

      if (nodePath.length() == (8 + 1 + 8)) {
        return true;
      } else {
        return false;
      }
    }

    protected boolean isContain(String path) {
      if (nodePath == null) {
        logger.warn("Create VirtualTopology error. Reason : isContain() nodeList is empty.");
        throw new IllegalArgumentException();
      }
      return nodePath.contains(path);
    }

    protected boolean isContain(DiNodePair pair) {
      if (nodePath == null) {
        logger.warn("Create VirtualTopology error. Reason : isContain() nodeList is empty.");
        throw new IllegalArgumentException();
      }
      if (pair.getFrom().getNodeType().equals(DiNodeType.INOUT)) {
        return nodePath.startsWith(pair.getTo().getNodeNoString());
      } else if (pair.getTo().getNodeType().equals(DiNodeType.INOUT)) {
        return nodePath.endsWith(pair.getFrom().getNodeNoString());
      } else {
        return nodePath.contains(pair.getNodePath());
      }
    }

    @Override
    public int compareTo(DiNodePair sortDNode) {
      return compValue.compareTo(sortDNode.compValue);
    }

    protected String getNodePath() {
      return nodePath;
    }

    public void setNodePath(String nodePath) {
      this.nodePath = nodePath;
    }

    public DiNode getFrom() {
      return from;
    }

    public void setFrom(DiNode from) {
      this.from = from;
    }

    public DiNode getTo() {
      return to;
    }

    public void setTo(DiNode to) {
      this.to = to;
    }

    public String getCompValue() {
      return compValue;
    }

    public void setCompValue(String compValue) {
      this.compValue = compValue;
    }

    public Double getTrafficValue() {
      return trafficValue;
    }

    public void setTrafficValue(Double trafficValue) {
      this.trafficValue = trafficValue;
    }

  }

}
