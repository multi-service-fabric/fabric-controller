
package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationVrrpEcEntity {

  @SerializedName("group_id")
  private Integer groupId;

  @SerializedName("role")
  private String role;

  @SerializedName("virtual_ipv4_address")
  private String virtualIpv4Addr;

  @SerializedName("virtual_ipv6_address")
  private String virtualIpv6Addr;

  @SerializedName("tracking_ifs")
  private List<OperationTrackingIfEcEntity> trackingIfList;

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

  public List<OperationTrackingIfEcEntity> getTrackingIfList() {
    return trackingIfList;
  }

  public void setTrackingIfList(List<OperationTrackingIfEcEntity> trackingIfList) {
    this.trackingIfList = trackingIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
