package msf.fc.traffic.tm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.rest.ec.traffic.data.entity.InternalLinkEcEntity;
import msf.fc.rest.ec.traffic.data.entity.L2CpEcEntity;
import msf.fc.rest.ec.traffic.data.entity.L3CpEcEntity;
import msf.fc.rest.ec.traffic.data.entity.SwitchTrafficEcEntity;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficVirtualTopology.CommonSpineDiNode;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodePair;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodeType;

public class TrafficXtxt extends AbstractTrafficFileController {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficAtxt.class);
  private TrafficCommonData trafficCommonData;

  public static final String XTXT_FILE = "X.txt";

  public TrafficXtxt() {
    trafficCommonData = TrafficCommonData.getInstance();
  }

  @Override
  void createFile() throws MsfException, InterruptedException {

    logger.methodStart();

    File xtxt = new File(trafficCommonData.getTrafficTmInputFilePath(), XTXT_FILE);

    try (FileOutputStream out = new FileOutputStream(xtxt)) {
      TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

      if (!checkWriteFile(xtxt)) {
        logger.warn("Create X.txt error. Reason : CheckWriteFile error.");
        throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "X.txt write error.");
      }

      List<DiNodePair> inoutPair = vt.getInOutPair();

      out.write(("X " + inoutPair.size() + "\n").getBytes());

      for (DiNodePair pair : inoutPair) {


        if ((DiNodeType.CommonSpine.equals(pair.getFrom().getNodeType()))
            || (DiNodeType.CommonSpine.equals(pair.getTo().getNodeType()))) {

          if ((DiNodeType.INOUT.equals(pair.getFrom().getNodeType()))
              || (DiNodeType.INOUT.equals(pair.getTo().getNodeType()))) {
            spineInout(pair, out);
          } else {
            spineNonInout(pair, out);
          }

        } else {

          if (((DiNodeType.L2Leaf.equals(pair.getFrom().getNodeType()))
              && (DiNodeType.INOUT.equals(pair.getTo().getNodeType())))
              || ((DiNodeType.L3Leaf.equals(pair.getFrom().getNodeType()))
                  && (DiNodeType.INOUT.equals(pair.getTo().getNodeType())))
              || ((DiNodeType.INOUT.equals(pair.getFrom().getNodeType()))
                  && (DiNodeType.L3Leaf.equals(pair.getTo().getNodeType())))
              || ((DiNodeType.INOUT.equals(pair.getFrom().getNodeType()))
                  && (DiNodeType.L2Leaf.equals(pair.getTo().getNodeType())))) {
            leafInout(pair, out);

          } else {

            if (((DiNodeType.L2Leaf.equals(pair.getFrom().getNodeType()))
                && (DiNodeType.L2EP.equals(pair.getTo().getNodeType())))
                || ((DiNodeType.L2EP.equals(pair.getFrom().getNodeType()))
                    && (DiNodeType.L2Leaf.equals(pair.getTo().getNodeType())))
                || ((DiNodeType.L2EP.equals(pair.getFrom().getNodeType()))
                    && (DiNodeType.INOUT.equals(pair.getTo().getNodeType())))
                || ((DiNodeType.INOUT.equals(pair.getFrom().getNodeType()))
                    && (DiNodeType.L2EP.equals(pair.getTo().getNodeType())))) {

              l2LeafL2Ep(pair, out);

            } else {
              l3LeafL3Cp(pair, out);
            }
          }

        }
      }
    } catch (IOException exp) {
      logger.warn("Create X.txt error. Reason : Catch IOException.", exp);
      throw new MsfException(ErrorCode.FILE_WRITE_ERROR, "X.txt Catch IOException.");
    } finally {
      logger.methodEnd();
    }
  }

  private void spineInout(DiNodePair pair, OutputStream out) throws IOException {
    out.write(String.format("%.6f\n", 0.0d).getBytes());
  }

  private void spineNonInout(DiNodePair pair, OutputStream out) throws IOException, InterruptedException, MsfException {
    try {
      logger.methodStart();
      double traffic = 0d;

      if (DiNodeType.CommonSpine.equals(pair.getFrom().getNodeType())) {
        for (Node node : ((CommonSpineDiNode) pair.getFrom()).getSpineNodeList()) {
          for (LagIf lagIf : node.getLagIfs()) {
            if (lagIf.getOppositeNode().getNodeId().equals(pair.getTo().getNode().getNodeId())) {

              if (lagIf.getInternalLinkIf() != null) {
                Integer internalLinkIfId = lagIf.getInternalLinkIf().getInternalLinkIfId();

                List<SwitchTrafficEcEntity> nodeList = trafficCommonData.getTrafficResponse().getSwitchTrafficList();
                for (SwitchTrafficEcEntity ecNode : nodeList) {
                  for (InternalLinkEcEntity ecEntity : ecNode.getTrafficValue().getInternalLinkList()) {
                    if (internalLinkIfId.equals(Integer.parseInt(ecEntity.getInternalLinkIfId()))) {
                      traffic += ecEntity.getSendRate();
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        for (Node node : ((CommonSpineDiNode) pair.getTo()).getSpineNodeList()) {
          for (LagIf lagIf : node.getLagIfs()) {
            if (lagIf.getOppositeNode().getNodeId().equals(pair.getFrom().getNode().getNodeId())) {

              if (lagIf.getInternalLinkIf() != null) {
                Integer internalLinkIfId = lagIf.getInternalLinkIf().getInternalLinkIfId();

                List<SwitchTrafficEcEntity> nodeList = trafficCommonData.getTrafficResponse().getSwitchTrafficList();
                for (SwitchTrafficEcEntity ecNode : nodeList) {
                  for (InternalLinkEcEntity ecEntity : ecNode.getTrafficValue().getInternalLinkList()) {
                    if (internalLinkIfId.equals(Integer.parseInt(ecEntity.getInternalLinkIfId()))) {
                      traffic += ecEntity.getReceiveRate();
                    }
                  }
                }
              }
            }
          }
        }
      }
      out.write(String.format("%.6f\n", traffic).getBytes());
    } finally {
      logger.methodEnd();
    }
  }

  private void leafInout(DiNodePair pair, OutputStream out) throws IOException {
    out.write(String.format("%.6f\n", 0.0d).getBytes());
  }

  private void l2LeafL2Ep(DiNodePair pair, OutputStream out) throws IOException, InterruptedException, MsfException {
    try {
      logger.methodStart();
      EdgePoint edgePoint = null;
      double traffic = 0d;
      boolean isTo = false;

      if (DiNodeType.L2EP.equals(pair.getFrom().getNodeType())) {
        edgePoint = pair.getFrom().getEdgePoint();
        if (DiNodeType.INOUT.equals(pair.getTo().getNodeType())) {
          isTo = true;
        }
      } else if (DiNodeType.L2EP.equals(pair.getTo().getNodeType())) {
        isTo = true;
        edgePoint = pair.getTo().getEdgePoint();
        if (DiNodeType.INOUT.equals(pair.getFrom().getNodeType())) {
          isTo = false;
        }
      }

      if (edgePoint == null) {
        out.write(String.format("%.6f\n", traffic).getBytes());
        return;
      }

      Integer lagIfId = null;
      String physicalIfId = null;
      if (edgePoint.getLagIfInfoId() != null) {
        for (LagIf lagIf : pair.getFrom().getNode().getLagIfs()) {
          if (edgePoint.getLagIfInfoId().equals(lagIf.getLagIfInfoId())) {
            lagIfId = lagIf.getLagIfId();
            break;
          }
        }
      } else {
        if (edgePoint.getPhysicalIfInfoId() != null) {
          for (PhysicalIf pif : pair.getFrom().getNode().getPhysicalIfs()) {
            if (edgePoint.getPhysicalIfInfoId().equals(pif.getPhysicalIfInfoId())) {
              physicalIfId = pif.getPhysicalIfId();
              break;
            }
          }
        } else {
          logger.warn("Both LagIF ID and PhysicalIF ID nothing.", ErrorCode.UNDEFINED_ERROR);
          throw new IllegalArgumentException();
        }
      }

      List<SwitchTrafficEcEntity> nodeList = trafficCommonData.getTrafficResponse().getSwitchTrafficList();
      for (SwitchTrafficEcEntity ecNode : nodeList) {

        if (ecNode.getNodeId() == null) {
          logger.debug("node_id is null. ");
          continue;
        }
        if (!pair.getFrom().getNode().getNodeId().toString().equals(ecNode.getNodeId())) {
          continue;
        }

        if (ecNode.getTrafficValue().getL2CpList() != null) {
          for (L2CpEcEntity ecEntity : ecNode.getTrafficValue().getL2CpList()) {
            if (lagIfId != null) {
              if ((ecEntity.getLagIfId() != null) && (lagIfId.equals(Integer.parseInt(ecEntity.getLagIfId())))) {
                if (isTo) {
                  traffic += ecEntity.getSendRate();
                } else {
                  traffic += ecEntity.getReceiveRate();
                }
              }
            } else if (physicalIfId != null) {
              if (physicalIfId.equals(ecEntity.getPhysicalIfId())) {
                if (isTo) {
                  traffic += ecEntity.getSendRate();
                } else {
                  traffic += ecEntity.getReceiveRate();
                }
              }
            }
          }
        }
      }

      String value = String.format("%.6f\n", traffic);
      logger.debug(pair.getNodePath() + " traffic value : " + value);

      out.write(value.getBytes());
    } finally {
      logger.methodEnd();
    }
  }

  private void l3LeafL3Cp(DiNodePair pair, OutputStream out) throws IOException, InterruptedException {
    try {
      logger.methodStart();
      L3Cp l3Cp = null;
      boolean isTo = false;

      double traffic = 0d;

      if (DiNodeType.L3CP.equals(pair.getFrom().getNodeType())) {
        l3Cp = pair.getFrom().getL3Cp();
        if (DiNodeType.INOUT.equals(pair.getTo().getNodeType())) {
          isTo = true;
        }
      } else if (DiNodeType.L3CP.equals(pair.getTo().getNodeType())) {
        isTo = true;
        l3Cp = pair.getTo().getL3Cp();
        if (DiNodeType.INOUT.equals(pair.getFrom().getNodeType())) {
          isTo = false;
        }
      }

      if (l3Cp == null) {
        out.write(String.format("%.6f\n", traffic).getBytes());
        return;
      }

      String sliceId = l3Cp.getId().getSliceId();
      String cpId = l3Cp.getId().getCpId();

      List<SwitchTrafficEcEntity> nodeList = trafficCommonData.getTrafficResponse().getSwitchTrafficList();
      for (SwitchTrafficEcEntity ecNode : nodeList) {
        if (ecNode.getTrafficValue().getL3CpList() != null) {
          for (L3CpEcEntity ecEntity : ecNode.getTrafficValue().getL3CpList()) {
            if ((sliceId.equals(ecEntity.getSliceId())) && (cpId.equals(ecEntity.getCpId()))) {
              if (isTo) {
                traffic += ecEntity.getSendRate();
              } else {
                traffic += ecEntity.getReceiveRate();
              }
            }
          }
        }
      }

      out.write(String.format("%.6f\n", traffic).getBytes());
    } finally {
      logger.methodEnd();
    }
  }
}
