
package msf.fc.rest.ec.node.recovernode.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InterfaceType;

public class RecoverInternalLinkIfListEcEntity {

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

  @SerializedName("opposite_node_id")
  private String oppositeNodeId;

  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  public InterfaceType getIfTypeEnum() {
    return InterfaceType.getEnumFromMessage(ifType);
  }

  public void setIfTypeEnum(InterfaceType ifType) {
    this.ifType = ifType.getMessage();
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  public String getOppositeNodeId() {
    return oppositeNodeId;
  }

  public void setOppositeNodeId(String oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
