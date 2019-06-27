
package msf.fc.rest.ec.node.interfaces.internallink.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InterfaceType;

public class InternalLinkIfEcEntity {

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

  @SerializedName("cost")
  private Integer cost;

  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  public Integer getCost() {
    return cost;
  }

  public void setCost(Integer cost) {
    this.cost = cost;
  }

  public InterfaceType getIfTypeEnum() {
    return InterfaceType.getEnumFromMessage(ifType);
  }

  public void setIfTypeEnum(InterfaceType ifType) {
    this.ifType = ifType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
