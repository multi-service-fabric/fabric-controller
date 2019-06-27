
package msf.fc.node.interfaces.clusterlinkifs;

import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfEntity;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfPhysicalLagLinkEntity;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfPhysicalLinkEntity;

/**
 * Abstract class to implement the common process of the inter-cluster link
 * interface-related processing in the configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractClusterLinkInterfaceScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractClusterLinkInterfaceScenarioBase.class);

  protected ClusterLinkIfEntity getClusterLinkInterfaceEntity(FcClusterLinkIf fcClusterLinkIf) throws MsfException {
    try {
      logger.methodStart();
      ClusterLinkIfEntity clusterLinkIfEntity = new ClusterLinkIfEntity();
      clusterLinkIfEntity.setClusterLinkIfId(String.valueOf(fcClusterLinkIf.getClusterLinkIfId()));
      if (fcClusterLinkIf.getLagIf() != null) {
        ClusterLinkIfPhysicalLagLinkEntity lagLink = new ClusterLinkIfPhysicalLagLinkEntity();
        lagLink.setNodeId(String.valueOf(fcClusterLinkIf.getLagIf().getNode().getNodeId()));
        lagLink.setLagIfId(String.valueOf(fcClusterLinkIf.getLagIf().getLagIfId()));

        clusterLinkIfEntity.setLagLink(lagLink);
      } else {
        ClusterLinkIfPhysicalLinkEntity physicalLink = new ClusterLinkIfPhysicalLinkEntity();
        if (fcClusterLinkIf.getPhysicalIf() != null) {
          physicalLink.setNodeId(String.valueOf(fcClusterLinkIf.getPhysicalIf().getNode().getNodeId()));
          physicalLink.setPhysicalIfId(fcClusterLinkIf.getPhysicalIf().getPhysicalIfId());
        } else {
          physicalLink.setNodeId(String.valueOf(fcClusterLinkIf.getBreakoutIf().getNode().getNodeId()));
          physicalLink.setBreakoutIfId(fcClusterLinkIf.getBreakoutIf().getBreakoutIfId());
        }

        clusterLinkIfEntity.setPhysicalLink(physicalLink);
      }
      clusterLinkIfEntity.setIgpCost(fcClusterLinkIf.getIgpCost());
      clusterLinkIfEntity.setIpv4Address(fcClusterLinkIf.getIpv4Addr());
      clusterLinkIfEntity.setTrafficThreshold(fcClusterLinkIf.getTrafficThreshold());
      clusterLinkIfEntity.setPortStatus(fcClusterLinkIf.getPortStatus());
      return clusterLinkIfEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
