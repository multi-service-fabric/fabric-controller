
package msf.mfcfc.slice.cps.l3cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.RoleType;

public class L3CpVrrpEntity {

  @SerializedName("group_id")
  private Integer groupId;

  @SerializedName("role")
  private String role;

  @SerializedName("virtual_ipv4_address")
  private String virtualIpv4Address;

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

  public String getVirtualIpv4Address() {
    return virtualIpv4Address;
  }

  public void setVirtualIpv4Address(String virtualIpv4Address) {
    this.virtualIpv4Address = virtualIpv4Address;
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
