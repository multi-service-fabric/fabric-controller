
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeL2VpnEcEntity {

  @SerializedName("bgp")
  private NodeBgpNodeEcEntity bgpNode;

  @SerializedName("as")
  private NodeAsEcEntity as;

  public NodeBgpNodeEcEntity getBgpNode() {
    return bgpNode;
  }

  public void setBgpNode(NodeBgpNodeEcEntity bgpNode) {
    this.bgpNode = bgpNode;
  }

  public NodeAsEcEntity getAs() {
    return as;
  }

  public void setAs(NodeAsEcEntity as) {
    this.as = as;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
