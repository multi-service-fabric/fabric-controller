
package msf.mfc.node.interfaces.clusterlinkifs;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.node.interfaces.MfcAbstractInterfaceScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfEntity;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfPhysicalLagLinkEntity;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfPhysicalLinkEntity;

/**
 * Abstract class to implement the common process of inter-cluster link
 * interface-related processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractClusterLinkInterfaceScenarioBase<T extends RestRequestBase>
    extends MfcAbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractClusterLinkInterfaceScenarioBase.class);

  protected List<ClusterLinkIfEntity> getClusterLinkIfEntities(List<MfcClusterLinkIf> clusterLinkIfs)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIfs" }, new Object[] { clusterLinkIfs });
      List<ClusterLinkIfEntity> clusterLinkIfEntities = new ArrayList<>();
      for (MfcClusterLinkIf clusterLinkIf : clusterLinkIfs) {
        clusterLinkIfEntities.add(getClusterLinkIfData(clusterLinkIf));
      }
      return clusterLinkIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getClusterLinkIfIdList(List<MfcClusterLinkIf> clusterLinkIfs) {
    try {
      logger.methodStart();
      List<String> clusterLinkIfIdList = new ArrayList<>();
      for (MfcClusterLinkIf clusterLinkIf : clusterLinkIfs) {
        clusterLinkIfIdList.add(String.valueOf(clusterLinkIf.getClusterLinkIfId()));
      }
      return clusterLinkIfIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected ClusterLinkIfEntity getClusterLinkIfData(MfcClusterLinkIf clusterLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIf" }, new Object[] { clusterLinkIf });

      ClusterLinkIfEntity clusterLinkIfEntity = new ClusterLinkIfEntity();
      clusterLinkIfEntity.setClusterLinkIfId(String.valueOf(clusterLinkIf.getClusterLinkIfId()));
      clusterLinkIfEntity.setOppositeClusterId(String.valueOf(clusterLinkIf.getOppositeSwClusterId()));

      if (clusterLinkIf.getPhysicalLink() != null) {

        ClusterLinkIfPhysicalLinkEntity physicalLink = new ClusterLinkIfPhysicalLinkEntity();
        physicalLink.setNodeId(String.valueOf(clusterLinkIf.getPhysicalLink().getNodeId()));
        physicalLink.setPhysicalIfId(clusterLinkIf.getPhysicalLink().getPhysicalIfId());
        physicalLink.setBreakoutIfId(clusterLinkIf.getPhysicalLink().getBreakoutIfId());
        physicalLink.setOppositeNodeId(String.valueOf(clusterLinkIf.getPhysicalLink().getOppositeNodeId()));
        physicalLink.setOppositeIfId(clusterLinkIf.getPhysicalLink().getOppositePhysicalIfId());
        physicalLink.setOppositeBreakoutIfId(clusterLinkIf.getPhysicalLink().getOppositeBreakoutIfId());
        clusterLinkIfEntity.setPhysicalLink(physicalLink);
      } else {

        ClusterLinkIfPhysicalLagLinkEntity lagLink = new ClusterLinkIfPhysicalLagLinkEntity();
        lagLink.setNodeId(String.valueOf(clusterLinkIf.getLagLink().getNodeId()));
        lagLink.setLagIfId(String.valueOf(clusterLinkIf.getLagLink().getLagIfId()));
        lagLink.setOppositeNodeId(String.valueOf(clusterLinkIf.getLagLink().getOppositeNodeId()));
        lagLink.setOppositeLagIfId(String.valueOf(clusterLinkIf.getLagLink().getOppositeLagIfId()));
        clusterLinkIfEntity.setLagLink(lagLink);
      }
      clusterLinkIfEntity.setIgpCost(clusterLinkIf.getIgpCost());
      clusterLinkIfEntity.setPortStatus(clusterLinkIf.getPortStatus());
      clusterLinkIfEntity.setIpv4Address(clusterLinkIf.getIpv4Addr());
      clusterLinkIfEntity.setTrafficThreshold(clusterLinkIf.getTrafficThreshold());
      return clusterLinkIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

}
