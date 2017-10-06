package msf.fc.traffic.tm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiEdge;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNode;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodePair;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodeType;

public class TrafficAtxt extends AbstractTrafficFileController {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficAtxt.class);

  public static final String ATXT_FILE = "A.txt";

  @Override
  void createFile() throws MsfException, InterruptedException {

    logger.methodStart();

    TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();


    File atxt = new File(trafficCommonData.getTrafficTmInputFilePath(), ATXT_FILE);

    try (FileOutputStream out = new FileOutputStream(atxt)) {
      TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

      if (!checkWriteFile(atxt)) {
        logger.warn("Create A.txt error. Reason : CheckWriteFile error.");
        throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "A.txt write error.");
      }

      createPair();
      Set<DiNodePair> groundPair = vt.getGroundPair();
      List<DiNodePair> inOutPair = vt.getInOutPair();

      out.write(("A " + inOutPair.size() + " " + groundPair.size() + "\n").getBytes());

      int[][] matrix = createMatrix(groundPair, inOutPair);

      for (int io = 0; io < inOutPair.size(); io++) {
        for (int gr = 0; gr < groundPair.size(); gr++) {
          if (gr != (groundPair.size() - 1)) {
            out.write(("" + matrix[gr][io] + " ").getBytes());
          } else {
            out.write(("" + matrix[gr][io]).getBytes());
          }
        }
        out.write(("\n").getBytes());
      }

      out.write(("INOUT " + groundPair.size() + "\n").getBytes());

      for (DiNodePair pair : groundPair) {
        out.write(("" + pair.getFrom().getNodeNo() + " " + pair.getTo().getNodeNo() + "\n").getBytes());
      }

    } catch (IOException exp) {
      logger.warn("Create A.txt error. Reason : Catch IOException.", exp);
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "A.txt Catch IOException.");
    } finally {
      logger.methodEnd();
    }
  }

  protected void createPair() throws MsfException, InterruptedException {
    TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

    Set<DiNodePair> allPair = createAllPair(vt);
    if (allPair == null) {
      logger.warn("Create A.txt Error. Reason : Pair is nothing.");
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "A.txt Create error. allPair is null.");
    }

    HipsterDirectedGraph<DiNode, DiEdge> graph = vt.getGraph();

    calcShortestPath(allPair, graph);

    Set<DiNodePair> groundPair = createGroundPair(allPair);
    if (groundPair == null) {
      logger.warn("Create A.txt Error. Reason : GroundPair is nothing.");
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "A.txt Create error. groundPair is null.");
    }

    List<DiNodePair> inOutPair = createInOutPair(allPair);
    if (inOutPair == null) {
      logger.warn("Create A.txt Error. Reason : Link is nothing.");
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "A.txt Create error. inOutPair is null.");
    }

    vt.setAllPair(allPair);
    vt.setGroundPair(groundPair);
    vt.setInOutPair(inOutPair);
  }

  protected Set<DiNodePair> createAllPair(TrafficVirtualTopology vt) throws InterruptedException {
    try {
      logger.methodStart();
      TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();
      Set<DiNodePair> diNodePairSet = new TreeSet<DiNodePair>();
      for (DiNode diNode : vt.getDiNodeSet()) {
        for (DiNode diNodeSub : vt.getDiNodeSet()) {
            diNodePairSet.add(vt.new DiNodePair(diNode, diNodeSub));
          }
        }
      }
      return diNodePairSet;
    } finally {
      logger.methodEnd();
    }
  }

  protected void calcShortestPath(Set<DiNodePair> allPair, HipsterDirectedGraph<DiNode, DiEdge> graph)
      throws InterruptedException {
    try {
      logger.methodStart();
      TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();
      for (DiNodePair pair : allPair) {
        pair.calcPath(graph);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected Set<DiNodePair> createGroundPair(Set<DiNodePair> allPair) throws InterruptedException {
    try {
      logger.methodStart();
      Set<DiNodePair> groundPair = new TreeSet<DiNodePair>();
      TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();

      for (DiNodePair pair : allPair) {
        if ((!DiNodeType.INOUT.equals(pair.getFrom().getNodeType()))
            && (!DiNodeType.INOUT.equals(pair.getTo().getNodeType()))) {
          groundPair.add(pair);
        }
      }
      return groundPair;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<DiNodePair> createInOutPair(Set<DiNodePair> allPair) throws InterruptedException {
    try {
      logger.methodStart();
      TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();

      List<DiNodePair> inOutPairList = new LinkedList<DiNodePair>();
      Set<DiNodePair> nodeSet = new TreeSet<DiNodePair>();
      Set<DiNodePair> inoutSet = new TreeSet<DiNodePair>();
      for (DiNodePair pair : allPair) {
        pair.setLinkNo(null);
          if (pair.getFrom().getNodeType().equals(DiNodeType.INOUT)
              || pair.getTo().getNodeType().equals(DiNodeType.INOUT)) {
            inoutSet.add(pair);
          } else {
            nodeSet.add(pair);
          }
        }
      }

      int linkNo = 0;
      for (DiNodePair pair : nodeSet) {
        inOutPairList.add(pair);
        pair.setLinkNo(linkNo++);
      }
      for (DiNodePair pair : inoutSet) {
        inOutPairList.add(pair);
        pair.setLinkNo(linkNo++);
      }

      return inOutPairList;
    } finally {
      logger.methodEnd();
    }
  }

  protected int[][] createMatrix(Set<DiNodePair> groundPair, List<DiNodePair> inOutPair) {
    try {
      logger.methodStart();
      int[][] matrix = new int[groundPair.size()][inOutPair.size()];

      Iterator<DiNodePair> inOutIte = inOutPair.iterator();
      int counteri = 0;
      while (inOutIte.hasNext()) {
        DiNodePair inOut = inOutIte.next();

        Iterator<DiNodePair> groundIte = groundPair.iterator();
        int counterj = 0;
        while (groundIte.hasNext()) {
          DiNodePair ground = groundIte.next();
          matrix[counterj][counteri] = ground.isContain(inOut) ? 1 : 0;

          counterj++;
        }

        counteri++;
      }

      return matrix;
    } finally {
      logger.methodEnd();
    }
  }

}
