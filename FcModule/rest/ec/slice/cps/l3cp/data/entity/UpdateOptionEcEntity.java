package msf.fc.rest.ec.slice.cps.l3cp.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.CpUpdateOperationType;

public class UpdateOptionEcEntity {
  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("ipv6_address")
  private String ipv6Address;

  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;

  @SerializedName("ipv6_prefix")
  private Integer ipv6Prefix;

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("operation_type")
  private String operationType;

  @SerializedName("router_id")
  private String routerId;

  @SerializedName("static_routes")
  private List<StaticRouteEcEntity> staticRouteList;

  public String getIpv4Address() {
    return ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public String getIpv6Address() {
    return ipv6Address;
  }

  public void setIpv6Address(String ipv6Address) {
    this.ipv6Address = ipv6Address;
  }

  public Integer getIpv4Prefix() {
    return ipv4Prefix;
  }

  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  public Integer getIpv6Prefix() {
    return ipv6Prefix;
  }

  public void setIpv6Prefix(Integer ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public String getRouterId() {
    return routerId;
  }

  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  public List<StaticRouteEcEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<StaticRouteEcEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  public CpUpdateOperationType getOperationTypeEnum() {
    return CpUpdateOperationType.getEnumFromMessage(operationType);
  }

  public void setOperationTypeEnum(CpUpdateOperationType operationType) {
    this.operationType = operationType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
