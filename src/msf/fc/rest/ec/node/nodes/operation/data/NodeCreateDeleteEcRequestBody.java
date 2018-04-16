
package msf.fc.rest.ec.node.nodes.operation.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeUpdateEcEntity;

public class NodeCreateDeleteEcRequestBody {

  @SerializedName("action")
  private String action;

  @SerializedName("add_node_option")
  private NodeCreateEcEntity addNodeOption;

  @SerializedName("del_node_option")
  private NodeDeleteEcEntity delNodeOption;

  @SerializedName("update_node_option")
  private NodeUpdateEcEntity updateNodeOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public NodeCreateEcEntity getAddNodeOption() {
    return addNodeOption;
  }

  public void setAddNodeOption(NodeCreateEcEntity addNodeOption) {
    this.addNodeOption = addNodeOption;
  }

  public NodeDeleteEcEntity getDelNodeOption() {
    return delNodeOption;
  }

  public void setDelNodeOption(NodeDeleteEcEntity delNodeOption) {
    this.delNodeOption = delNodeOption;
  }

  public NodeUpdateEcEntity getUpdateNodeOption() {
    return updateNodeOption;
  }

  public void setUpdateNodeOption(NodeUpdateEcEntity updateNodeOption) {
    this.updateNodeOption = updateNodeOption;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
