
package msf.mfc.failure.search;

import java.util.HashMap;
import java.util.Map;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceUnitReachableStatus;
import msf.mfcfc.failure.search.CpData;
import msf.mfcfc.failure.search.DiNode;

public class MfcBLeafDiNode extends DiNode {

  private Integer clusterId;

  private Integer leafNodeId;

  private Map<MfcClusterLinkIf, FailureStatus> clusterLinkIfMap = new HashMap<>();

  private Map<CpData, SliceUnitReachableStatus> cpReachabilityMap = new HashMap<>();

  private Map<String, SliceUnitReachableStatus> bleafReachabilityMap = new HashMap<>();

  public MfcBLeafDiNode(int nodeNo) {
    super(nodeNo);
  }

  public Integer getClusterId() {
    return clusterId;
  }

  public void setClusterId(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public Integer getLeafNodeId() {
    return leafNodeId;
  }

  public void setLeafNodeId(Integer leafNodeId) {
    this.leafNodeId = leafNodeId;
  }

  public Map<MfcClusterLinkIf, FailureStatus> getClusterLinkIfMap() {
    return clusterLinkIfMap;
  }

  public void setClusterLinkIfMap(Map<MfcClusterLinkIf, FailureStatus> clusterLinkIfMap) {
    this.clusterLinkIfMap = clusterLinkIfMap;
  }

  public Map<CpData, SliceUnitReachableStatus> getCpReachabilityMap() {
    return cpReachabilityMap;
  }

  public void setCpReachabilityMap(Map<CpData, SliceUnitReachableStatus> cpReachabilityMap) {
    this.cpReachabilityMap = cpReachabilityMap;
  }

  public Map<String, SliceUnitReachableStatus> getBleafReachabilityMap() {
    return bleafReachabilityMap;
  }

  public void setBleafReachabilityMap(Map<String, SliceUnitReachableStatus> bleafReachabilityMap) {
    this.bleafReachabilityMap = bleafReachabilityMap;
  }

  @Override
  public String getStrForNodeSpecify() {
    return makeStrForBLeafSpecify(clusterId, leafNodeId);
  }

  public static String makeStrForBLeafSpecify(int clusterId, int leafNodeId) {
    return "[" + clusterId + "-" + leafNodeId + "]";
  }

}
