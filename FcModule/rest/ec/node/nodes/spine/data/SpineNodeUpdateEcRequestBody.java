package msf.fc.rest.ec.node.nodes.spine.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.EcNodeOperationAction;
import msf.fc.rest.ec.node.nodes.spine.data.entity.AddNodeOptionEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.DelNodeOptionEcEntity;

public class SpineNodeUpdateEcRequestBody {

  @SerializedName("action")
  private String action;
  @SerializedName("add_node_option")
  private AddNodeOptionEcEntity addNodeOption;
  @SerializedName("del_node_option")
  private DelNodeOptionEcEntity delNodeOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public EcNodeOperationAction getActionEnum() {
    return EcNodeOperationAction.getEnumFromMessage(action);
  }

  public void setActionEnum(EcNodeOperationAction action) {
    this.action = action.getMessage();
  }

  public AddNodeOptionEcEntity getAddNodeOption() {
    return addNodeOption;
  }

  public void setAddNodeOption(AddNodeOptionEcEntity addNodeOption) {
    this.addNodeOption = addNodeOption;
  }

  public DelNodeOptionEcEntity getDelNodeOption() {
    return delNodeOption;
  }

  public void setDelNodeOption(DelNodeOptionEcEntity delNodeOption) {
    this.delNodeOption = delNodeOption;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
