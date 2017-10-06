package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.RoleType;

public class VrrpEcEntity {
  @SerializedName("group_id")
  private Integer groupId;

  @SerializedName("role")
  private String role;

  @SerializedName("virtual_ipv4_address")
  private String virtualIpv4Addr;

  @SerializedName("virtual_ipv6_address")
  private String virtualIpv6Addr;

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getVirtualIpv4Addr() {
    return virtualIpv4Addr;
  }

  public void setVirtualIpv4Addr(String virtualIpv4Addr) {
    this.virtualIpv4Addr = virtualIpv4Addr;
  }

  public String getVirtualIpv6Addr() {
    return virtualIpv6Addr;
  }

  public void setVirtualIpv6Addr(String virtualIpv6Addr) {
    this.virtualIpv6Addr = virtualIpv6Addr;
  }

  public RoleType getRoleEnum() {
    return RoleType.getEnumFromMessage(role);
  }

  public void setRoleEnum(RoleType role) {
    this.role = role.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
