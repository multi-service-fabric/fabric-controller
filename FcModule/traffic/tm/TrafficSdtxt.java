package msf.fc.traffic.tm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.L2Cp;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNode;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodePair;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodeType;

public class TrafficSdtxt extends AbstractTrafficFileController {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficSdtxt.class);

  public static final String SDTXT_FILE = "sd.txt";

  @Override
  void createFile() throws MsfException, InterruptedException {

    logger.methodStart();

    TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();

    File sdtxt = new File(trafficCommonData.getTrafficTmInputFilePath(), SDTXT_FILE);

    try (FileOutputStream out = new FileOutputStream(sdtxt)) {

      TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

      if (!checkWriteFile(sdtxt)) {
        logger.warn("Create sd.txt error. Reason : CheckWriteFile error.");
        throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "sd.txt write error.");
      }

      Set<DiNodePair> groundPairCopy = new TreeSet<>(vt.getGroundPair());

      Map<String, List<DiNode>> l2SliceMap = getL2SliceMap(vt.getVertexSet());
      pickupList(groundPairCopy, l2SliceMap);

      Map<String, List<DiNode>> l3SliceMap = getL3SliceMap(vt.getVertexSet());
      pickupList(groundPairCopy, l3SliceMap);

      for (DiNodePair pair : groundPairCopy) {
        out.write((pair.getFrom().getNodeNo() + "," + pair.getTo().getNodeNo() + ",0" + "\n").getBytes());
      }

    } catch (IOException exp) {
      logger.warn("Create sd.txt error. Reason : Catch IOException.", exp);
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "sd.txt Catch IOException.");
    } finally {
      logger.methodEnd();
    }
  }

  private void pickupList(Set<DiNodePair> groundPairCopy, Map<String, List<DiNode>> sliceMap)
      throws InterruptedException {

    TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

    for (List<DiNode> l2l3List : sliceMap.values()) {
      TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();
      for (DiNode from : l2l3List) {
        for (DiNode to : l2l3List) {

            DiNodePair pair = getPair(from, to, vt.getAllPair());
            Iterator<DiNodePair> ite = groundPairCopy.iterator();
            while (ite.hasNext()) {
              DiNodePair targetPair = ite.next();

              if ((!(targetPair.getNodePath().equals(""))) && (pair.isContain(targetPair.getNodePath()))) {
                ite.remove();
              }
            }
          }
        }
      }
    }
  }

  private DiNodePair getPair(DiNode from, DiNode to, Set<DiNodePair> allPair) {
    try {
      logger.methodStart();
      for (DiNodePair pair : allPair) {
        if ((pair.getFrom().getNodeNo() == from.getNodeNo()) && (pair.getTo().getNodeNo() == to.getNodeNo())) {
          return pair;
        }
      }

      return null;
    } finally {
      logger.methodEnd();
    }
  }

  private Map<String, List<DiNode>> getL2SliceMap(Set<DiNode> vertexSet) throws MsfException {
    try {
      logger.methodStart();
      Map<String, List<DiNode>> l2SliceMap = new HashMap<>();

      for (DiNode diNode : vertexSet) {
        if (DiNodeType.L2EP.equals(diNode.getNodeType())) {
          List<L2Cp> l2CpList = diNode.getEdgePoint().getL2Cps();
          for (L2Cp l2Cp : l2CpList) {
            if (ActiveStatus.INACTIVE.equals(l2Cp.getStatusEnum())) {
              continue;
            }

            List<DiNode> l2NodeList = l2SliceMap.get(l2Cp.getId().getSliceId());
            if (l2NodeList == null) {
              l2NodeList = new LinkedList<>();
              l2SliceMap.put(l2Cp.getId().getSliceId(), l2NodeList);
            }
            l2NodeList.add(diNode);
          }
        }
      }

      return l2SliceMap;
    } finally {
      logger.methodEnd();
    }
  }

  private Map<String, List<DiNode>> getL3SliceMap(Set<DiNode> vertexSet) {
    try {
      logger.methodStart();
      Map<String, List<DiNode>> l3SliceMap = new HashMap<>();

      for (DiNode diNode : vertexSet) {
        if (DiNodeType.L3CP.equals(diNode.getNodeType())) {

          List<DiNode> l3NodeList = l3SliceMap.get(diNode.getL3Cp().getId().getSliceId());
          if (l3NodeList == null) {
            l3NodeList = new LinkedList<>();
            l3SliceMap.put(diNode.getL3Cp().getId().getSliceId(), l3NodeList);
          }
          l3NodeList.add(diNode);
        }
      }
      return l3SliceMap;
    } finally {
      logger.methodEnd();
    }
  }
}
