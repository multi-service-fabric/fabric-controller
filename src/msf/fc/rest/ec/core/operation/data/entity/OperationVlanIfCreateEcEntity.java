package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class OperationVlanIfCreateEcEntity {

  
  @SerializedName("vlan_if_id")
  private String vlanIfId;

  
  @SerializedName("base_if")
  private OperationBaseIfEcEntity baseIf;

  
  @SerializedName("vlan_id")
  private Integer vlanId;

  
  @SerializedName("mtu")
  private Integer mtu;

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("ipv6_address")
  private String ipv6Address;

  
  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;

  
  @SerializedName("ipv6_prefix")
  private Integer ipv6Prefix;

  
  @SerializedName("bgp")
  private OperationBgpEcEntity bgp;

  
  @SerializedName("static_routes")
  private List<OperationStaticRouteEcEntity> staticRouteList;

  
  @SerializedName("vrrp")
  private OperationVrrpEcEntity vrrp;

  
  @SerializedName("qos")
  private OperationQosEcEntity qos;

  
  @SerializedName("route_distinguisher")
  private String routeDistinguisher;

  
  public String getVlanIfId() {
    return vlanIfId;
  }

  
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  
  public OperationBaseIfEcEntity getBaseIf() {
    return baseIf;
  }

  
  public void setBaseIf(OperationBaseIfEcEntity baseIf) {
    this.baseIf = baseIf;
  }

  
  public Integer getVlanId() {
    return vlanId;
  }

  
  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  
  public Integer getMtu() {
    return mtu;
  }

  
  public void setMtu(Integer mtu) {
    this.mtu = mtu;
  }

  
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

  
  public OperationBgpEcEntity getBgp() {
    return bgp;
  }

  
  public void setBgp(OperationBgpEcEntity bgp) {
    this.bgp = bgp;
  }

  
  public List<OperationStaticRouteEcEntity> getStaticRouteList() {
    return staticRouteList;
  }

  
  public void setStaticRouteList(List<OperationStaticRouteEcEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  
  public OperationVrrpEcEntity getVrrp() {
    return vrrp;
  }

  
  public void setVrrp(OperationVrrpEcEntity vrrp) {
    this.vrrp = vrrp;
  }

  
  public OperationQosEcEntity getQos() {
    return qos;
  }

  
  public void setQos(OperationQosEcEntity qos) {
    this.qos = qos;
  }

  
  public String getRouteDistinguisher() {
    return routeDistinguisher;
  }

  
  public void setRouteDistinguisher(String routeDistinguisher) {
    this.routeDistinguisher = routeDistinguisher;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
