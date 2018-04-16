
package msf.mfcfc.slice.cps.l3cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.RoleType;

public class L3CpBgpEntity {

  @SerializedName("role")
  private String role;

  @SerializedName("neighbor_as")
  private Integer neighborAs;

  @SerializedName("neighbor_ipv4_address")
  private String neighborIpv4Address;

  @SerializedName("neighbor_ipv6_address")
  private String neighborIpv6Address;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Integer getNeighborAs() {
    return neighborAs;
  }

  public void setNeighborAs(Integer neighborAs) {
    this.neighborAs = neighborAs;
  }

  public String getNeighborIpv4Address() {
    return neighborIpv4Address;
  }

  public void setNeighborIpv4Address(String neighborIpv4Address) {
    this.neighborIpv4Address = neighborIpv4Address;
  }

  public String getNeighborIpv6Address() {
    return neighborIpv6Address;
  }

  public void setNeighborIpv6Address(String neighborIpv6Address) {
    this.neighborIpv6Address = neighborIpv6Address;
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
