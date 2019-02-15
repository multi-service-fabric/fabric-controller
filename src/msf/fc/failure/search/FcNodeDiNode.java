
package msf.fc.failure.search;

import java.util.HashMap;
import java.util.Map;

import msf.fc.common.data.FcNode;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.failure.search.DiNode;

/**
 * Class for nodes used by Dijkstra's algorithm with node information.
 *
 * @author NTT
 *
 */
public class FcNodeDiNode extends DiNode {
  public FcNodeDiNode(int nodeNo) {
    super(nodeNo);
  }

  protected FcNode fcNode;

  protected Map<FcNode, FailureStatus> oppositeNodeMap = new HashMap<>();

  public FcNode getFcNode() {
    return fcNode;
  }

  public void setFcNode(FcNode fcNode) {
    this.fcNode = fcNode;
  }

  public Map<FcNode, FailureStatus> getOppositeNodeMap() {
    return oppositeNodeMap;
  }

  public void setOppositeNodeMap(Map<FcNode, FailureStatus> oppositeNodeMap) {
    this.oppositeNodeMap = oppositeNodeMap;
  }

  @Override
  public String getStrForNodeSpecify() {
    return fcNode.getNodeTypeEnum().getSingularMessage() + ":" + fcNode.getNodeId();
  }

}
