package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class CreateL3CpEcEntity {

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("base_if")
  private L3BaseIfEcEntity baseIf;

  @SerializedName("vlan_id")
  private Integer vlanId;
  @SerializedName("mtu")
  private Integer mtu;

  @SerializedName("ipv4_address")
  private String ipv4Addr;
  @SerializedName("ipv6_address")
  private String ipv6Addr;
  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;
  @SerializedName("ipv6_prefix")
  private Integer ipv6Prefix;
  @SerializedName("bgp")
  private BgpEcEntity bgp;
  @SerializedName("ospf")
  private OspfEcEntity ospf;
  @SerializedName("static_routes")
  private List<StaticRouteEcEntity> staticRouteList;
  @SerializedName("vrrp")
  private VrrpEcEntity vrrp;

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public L3BaseIfEcEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(L3BaseIfEcEntity baseIf) {
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

  public String getIpv4Addr() {
    return ipv4Addr;
  }

  public void setIpv4Addr(String ipv4Addr) {
    this.ipv4Addr = ipv4Addr;
  }

  public String getIpv6Addr() {
    return ipv6Addr;
  }

  public void setIpv6Addr(String ipv6Addr) {
    this.ipv6Addr = ipv6Addr;
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

  public BgpEcEntity getBgp() {
    return bgp;
  }

  public void setBgp(BgpEcEntity bgp) {
    this.bgp = bgp;
  }

  public OspfEcEntity getOspf() {
    return ospf;
  }

  public void setOspf(OspfEcEntity ospf) {
    this.ospf = ospf;
  }

  public List<StaticRouteEcEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<StaticRouteEcEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  public VrrpEcEntity getVrrp() {
    return vrrp;
  }

  public void setVrrp(VrrpEcEntity vrrp) {
    this.vrrp = vrrp;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
