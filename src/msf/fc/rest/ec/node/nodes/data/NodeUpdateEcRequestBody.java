
package msf.fc.rest.ec.node.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.nodes.data.entity.NodeUpdateEcEntity;

public class NodeUpdateEcRequestBody {

  @SerializedName("node")
  private NodeUpdateEcEntity node;

  public NodeUpdateEcEntity getNode() {
    return node;
  }

  public void setNode(NodeUpdateEcEntity node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
