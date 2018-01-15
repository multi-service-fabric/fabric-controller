package msf.fc.rest.ec.node.nodes.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeInternalLinkIfCreateEcEntity {

  
  @SerializedName("if_type")
  private String ifType;

  
  @SerializedName("if_id")
  private String ifId;

  
  @SerializedName("lag_member")
  private List<NodeLagMemberEcEntity> lagMemberList;

  
  @SerializedName("link_speed")
  private String linkSpeed;

  
  @SerializedName("link_ip_address")
  private String linkIpAddress;

  
  @SerializedName("prefix")
  private Integer prefix;

  
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

  
  public List<NodeLagMemberEcEntity> getLagMemberList() {
    return lagMemberList;
  }

  
  public void setLagMemberList(List<NodeLagMemberEcEntity> lagMemberList) {
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

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
