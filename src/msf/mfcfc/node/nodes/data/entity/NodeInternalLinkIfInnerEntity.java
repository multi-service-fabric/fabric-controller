
package msf.mfcfc.node.nodes.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InterfaceType;

public class NodeInternalLinkIfInnerEntity {

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

  @SerializedName("lag_member")
  private List<NodeLagMemberEntity> lagMemberList;

  @SerializedName("link_speed")
  private String linkSpeed;

  @SerializedName("link_ip_address")
  private String linkIpAddress;

  @SerializedName("prefix")
  private Integer prefix;

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

  public List<NodeLagMemberEntity> getLagMemberList() {
    return lagMemberList;
  }

  public void setLagMemberList(List<NodeLagMemberEntity> lagMemberList) {
    this.lagMemberList = lagMemberList;
  }

  public String getLinkSpeed() {
    return linkSpeed;
  }

  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  public String getLinkIpAddress() {
    return linkIpAddress;
  }

  public void setLinkIpAddress(String linkIpAddress) {
    this.linkIpAddress = linkIpAddress;
  }

  public Integer getPrefix() {
    return prefix;
  }

  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
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

  public void setIfTypeEnum(InterfaceType interfaceType) {
    this.ifType = interfaceType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
