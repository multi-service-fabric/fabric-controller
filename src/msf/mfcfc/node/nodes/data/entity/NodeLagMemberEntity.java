package msf.mfcfc.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InterfaceType;


public class NodeLagMemberEntity {

  
  @SerializedName("if_type")
  private String ifType;

  
  @SerializedName("if_id")
  private String ifId;

  
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

  
  public InterfaceType getIfTypeEnum() {
    return InterfaceType.getEnumFromMessage(ifType);
  }

  
  public void setIfTypeEnum(InterfaceType interfaceType) {
    this.ifType = interfaceType.getMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
