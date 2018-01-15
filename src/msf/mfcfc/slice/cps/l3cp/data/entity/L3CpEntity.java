
package msf.mfcfc.slice.cps.l3cp.data.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.L3ProtocolType;


public class L3CpEntity {

  
  @SerializedName("cp_id")
  private String cpId;

  
  @SerializedName("slice_id")
  private String sliceId;

  
  @SerializedName("cluster_id")
  private String clusterId;

  
  @SerializedName("edge_point_id")
  private String edgePointId;

  
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
  private L3CpBgpEntity bgp;

  
  @SerializedName("static_routes")
  private List<L3CpStaticRouteEntity> staticRouteList;

  
  @SerializedName("vrrp")
  private L3CpVrrpEntity vrrp;

  
  @SerializedName("qos")
  private L3CpQosEntity qos;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
  @SerializedName("support_protocols")
  private List<String> supportProtocolList;

  
  public String getCpId() {
    return cpId;
  }

  
  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  
  public String getSliceId() {
    return sliceId;
  }

  
  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  
  public String getClusterId() {
    return clusterId;
  }

  
  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  
  public String getEdgePointId() {
    return edgePointId;
  }

  
  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
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

  
  public L3CpBgpEntity getBgp() {
    return bgp;
  }

  
  public void setBgp(L3CpBgpEntity bgp) {
    this.bgp = bgp;
  }

  
  public List<L3CpStaticRouteEntity> getStaticRouteList() {
    return staticRouteList;
  }

  
  public void setStaticRouteList(List<L3CpStaticRouteEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  
  public L3CpVrrpEntity getVrrp() {
    return vrrp;
  }

  
  public void setVrrp(L3CpVrrpEntity vrrp) {
    this.vrrp = vrrp;
  }

  
  public L3CpQosEntity getQos() {
    return qos;
  }

  
  public void setQos(L3CpQosEntity qos) {
    this.qos = qos;
  }

  
  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  
  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  
  public List<String> getSupportProtocolList() {
    return supportProtocolList;
  }

  
  public void setSupportProtocolList(List<String> supportProtocolList) {
    this.supportProtocolList = supportProtocolList;
  }

  
  public List<L3ProtocolType> getSupportProtocolEnumList() {
    List<L3ProtocolType> enumList = new ArrayList<L3ProtocolType>();
    for (String protocol : supportProtocolList) {
      L3ProtocolType protocolEnum = L3ProtocolType.getEnumFromMessage(protocol);
      if (protocolEnum != null) {
        enumList.add(protocolEnum);
      }
    }
    return enumList;
  }

  
  public void setSupportProtocolEnumList(L3ProtocolType... enums) {
    List<String> newSupportProtocolList = new ArrayList<String>();
    for (L3ProtocolType protocolEnum : enums) {
      newSupportProtocolList.add(protocolEnum.getMessage());
    }
    this.supportProtocolList = newSupportProtocolList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
