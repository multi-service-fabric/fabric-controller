
package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VlanIfEcEntity {

  @SerializedName("vlan_if_id")
  private String vlanIfId;

  @SerializedName("vlan_id")
  private String vlanId;

  @SerializedName("if_name")
  private String ifName;

  @SerializedName("if_state")
  private String ifState;

  @SerializedName("base_if")
  private VlanIfBaseIfEcEntity baseIf;

  @SerializedName("mtu")
  private Integer mtu;

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("ipv6_address")
  private String ipv6Address;

  @SerializedName("ipv4_prefix")
  private String ipv4Prefix;

  @SerializedName("ipv6_prefix")
  private String ipv6Prefix;

  @SerializedName("port_mode")
  private String portMode;

  @SerializedName("bgp")
  private VlanIfBgpEcEntity bgp;

  @SerializedName("static_routes")
  private List<VlanIfStaticRouteEcEntity> staticRouteList;

  @SerializedName("vrrp")
  private VlanIfVrrpEcEntity vrrp;

  @SerializedName("qos")
  private VlanIfQosEcEntity qos;

  public String getVlanIfId() {
    return vlanIfId;
  }

  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  public String getVlanId() {
    return vlanId;
  }

  public void setVlanId(String vlanId) {
    this.vlanId = vlanId;
  }

  public String getIfName() {
    return ifName;
  }

  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  public String getIfState() {
    return ifState;
  }

  public void setIfState(String ifState) {
    this.ifState = ifState;
  }

  public VlanIfBaseIfEcEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(VlanIfBaseIfEcEntity baseIf) {
    this.baseIf = baseIf;
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

  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  public void setIpv4Prefix(String ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  public String getIpv6Prefix() {
    return ipv6Prefix;
  }

  public void setIpv6Prefix(String ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  public VlanIfBgpEcEntity getBgp() {
    return bgp;
  }

  public void setBgp(VlanIfBgpEcEntity bgp) {
    this.bgp = bgp;
  }

  public List<VlanIfStaticRouteEcEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<VlanIfStaticRouteEcEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  public VlanIfVrrpEcEntity getVrrp() {
    return vrrp;
  }

  public void setVrrp(VlanIfVrrpEcEntity vrrp) {
    this.vrrp = vrrp;
  }

  public VlanIfQosEcEntity getQos() {
    return qos;
  }

  public void setQos(VlanIfQosEcEntity qos) {
    this.qos = qos;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
