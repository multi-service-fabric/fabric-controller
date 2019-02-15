
package msf.fc.failure.search;

import msf.fc.common.data.FcNode;

/**
 * Class for nodes used by Dijkstra's algorithm with Spine node information.
 *
 */
public class FcSpineDiNode extends FcNodeDiNode {

  /**
   * Constructor.
   *
   * @param nodeNo
   *          Unique node number in the topology
   * @param fcNode
   *          Node information
   */
  public FcSpineDiNode(int nodeNo, FcNode fcNode) {
    super(nodeNo);
    this.fcNode = fcNode;
  }

  @Override
  public String getStrForNodeSpecify() {
    return "Spine:" + fcNode.getNodeId();
  }

}
