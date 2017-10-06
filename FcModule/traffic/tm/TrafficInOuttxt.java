package msf.fc.traffic.tm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNode;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodePair;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodeType;

public class TrafficInOuttxt extends AbstractTrafficFileController {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficInOuttxt.class);

  public static final String INOUTTXT_FILE = "INOUT.txt";

  @Override
  void createFile() throws MsfException, InterruptedException {

    logger.methodStart();

    TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();

    File inout = new File(trafficCommonData.getTrafficTmInputFilePath(), INOUTTXT_FILE);

    try (FileOutputStream out = new FileOutputStream(inout)) {
      TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

      if (!checkWriteFile(inout)) {
        logger.warn("Create INOUT.txt error. Reason : CheckWriteFile error.");
        throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "INOUT.txt write error.");
      }

      Set<DiNode> vertexSet = vt.getVertexSet();

      List<DiNodePair> inPairList = getInPair(vertexSet, vt);

      List<DiNodePair> outPairList = getOutPair(vertexSet, vt);

      out.write(("IN " + vertexSet.size() + "\n").getBytes());

      int counter = 0;
      for (DiNode diNode : vertexSet) {
        DiNodePair inPair = inPairList.get(counter);
        DiNodePair outPair = outPairList.get(counter);

        out.write((inPair.getLinkNo() + " " + diNode.getNodeNo() + " " + outPair.getLinkNo() + "\n").getBytes());
        counter++;
      }


      out.write(("OUT " + vertexSet.size() + "\n").getBytes());

      counter = 0;
      for (DiNode diNode : vertexSet) {
        DiNodePair inPair = inPairList.get(counter);
        DiNodePair outPair = outPairList.get(counter);

        out.write((outPair.getLinkNo() + " " + diNode.getNodeNo() + " " + inPair.getLinkNo() + "\n").getBytes());
        counter++;
      }
    } catch (IOException exp) {
      logger.warn("Create INOUT.txt error. Reason : Catch IOException.", exp);
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "INOUT.txt Catch IOException.");
    } catch (NullPointerException ne) {
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, "UNDEFINED_ERROR");
    } finally {
      logger.methodEnd();
    }
  }

  protected List<DiNodePair> getInPair(Set<DiNode> vertexSet, TrafficVirtualTopology vt) {
    try {
      logger.methodStart();
      List<DiNodePair> result = new LinkedList<>();
      for (DiNode diNode : vertexSet) {
        for (DiNodePair pair : vt.getInOutPair()) {
          if ((pair.getTo().getNodeNo() == diNode.getNodeNo())
              && (DiNodeType.INOUT.equals(pair.getFrom().getNodeType()))) {
            result.add(pair);
            break;
          }
        }

      }

      return result;

    } finally {
      logger.methodEnd();
    }
  }

  protected List<DiNodePair> getOutPair(Set<DiNode> vertexSet, TrafficVirtualTopology vt) {
    try {
      logger.methodStart();
      List<DiNodePair> result = new LinkedList<>();
      for (DiNode diNode : vertexSet) {
        for (DiNodePair pair : vt.getInOutPair()) {
          if ((pair.getFrom().getNodeNo() == diNode.getNodeNo())
              && (DiNodeType.INOUT.equals(pair.getTo().getNodeType()))) {
            result.add(pair);
            break;
          }
        }

      }

      return result;

    } finally {
      logger.methodEnd();
    }
  }

}
