
package msf.fc.failure.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcNode;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.failure.search.SliceUnitFailureEndPointData;

public class FcLeafDiNode extends FcNodeDiNode {

  protected Map<FcL2Cp, FailureStatus> l2CpMap = new HashMap<>();

  protected Map<FcL3Cp, FailureStatus> l3CpMap = new HashMap<>();

  protected Map<FcClusterLinkIf, FailureStatus> clusterLinkIfMap = new HashMap<>();

  protected boolean isBLeaf = false;

  public FcLeafDiNode(int nodeNo, FcNode fcNode) throws MsfException {
    super(nodeNo);
    this.fcNode = fcNode;
    isBLeaf = (LeafType.BORDER_LEAF.getCode() == fcNode.getLeafNode().getLeafType());
  }

  public Map<FcL2Cp, FailureStatus> getL2CpMap() {
    return l2CpMap;
  }

  public void setL2CpMap(Map<FcL2Cp, FailureStatus> l2CpMap) {
    this.l2CpMap = l2CpMap;
  }

  public Map<FcL3Cp, FailureStatus> getL3CpMap() {
    return l3CpMap;
  }

  public void setL3CpMap(Map<FcL3Cp, FailureStatus> l3CpMap) {
    this.l3CpMap = l3CpMap;
  }

  public Map<FcClusterLinkIf, FailureStatus> getClusterLinkIfMap() {
    return clusterLinkIfMap;
  }

  public void setClusterLinkIfMap(Map<FcClusterLinkIf, FailureStatus> clusterLinkIfMap) {
    this.clusterLinkIfMap = clusterLinkIfMap;
  }

  public boolean isBLeaf() {
    return isBLeaf;
  }

  @Override
  public String getStrForNodeSpecify() {
    if (isBLeaf()) {
      return "B-Leaf:" + fcNode.getNodeId();
    } else {
      return "Leaf:" + fcNode.getNodeId();
    }
  }

  public List<SliceUnitFailureEndPointData> getAllEndPointDataList() {
    List<SliceUnitFailureEndPointData> endPointDataList = new ArrayList<>();

    for (Map.Entry<FcL2Cp, FailureStatus> entry : l2CpMap.entrySet()) {
      SliceUnitFailureEndPointData endpointData = new SliceUnitFailureEndPointData(SliceType.L2_SLICE,
          entry.getKey().getId().getSliceId(), entry.getKey().getId().getCpId(), entry.getValue());
      endPointDataList.add(endpointData);
    }

    for (Map.Entry<FcL3Cp, FailureStatus> entry : l3CpMap.entrySet()) {
      SliceUnitFailureEndPointData endpointData = new SliceUnitFailureEndPointData(SliceType.L3_SLICE,
          entry.getKey().getId().getSliceId(), entry.getKey().getId().getCpId(), entry.getValue());
      endPointDataList.add(endpointData);
    }

    for (Map.Entry<FcClusterLinkIf, FailureStatus> entry : clusterLinkIfMap.entrySet()) {
      SliceUnitFailureEndPointData endpointData = new SliceUnitFailureEndPointData(null, null,
          entry.getKey().getClusterLinkIfId().toString(), entry.getValue());
      endPointDataList.add(endpointData);
    }

    return endPointDataList;
  }
}
