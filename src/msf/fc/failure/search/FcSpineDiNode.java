
package msf.fc.failure.search;

import msf.fc.common.data.FcNode;

public class FcSpineDiNode extends FcNodeDiNode {

  public FcSpineDiNode(int nodeNo, FcNode fcNode) {
    super(nodeNo);
    this.fcNode = fcNode;
  }

  @Override
  public String getStrForNodeSpecify() {
    return "Spine:" + fcNode.getNodeId();
  }

}
