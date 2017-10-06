package msf.fc.slice.cps.l3cp.data.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.L3ProtocolType;
import msf.fc.common.constant.ReserveStatus;

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

  @SerializedName("ipv4_addr")
  private String ipv4Addr;
  @SerializedName("ipv6_addr")
  private String ipv6Addr;
  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;
  @SerializedName("ipv6_prefix")
  private Integer ipv6Prefix;
  @SerializedName("bgp")
  private BgpEntity bgp;
  @SerializedName("ospf")
  private OspfEntity ospf;
  @SerializedName("static_routes")
  private List<StaticRouteEntity> staticRouteList;
  @SerializedName("vrrp")
  private VrrpEntity vrrp;

  @SerializedName("status")
  private String status;
  @SerializedName("reservation_status")
  private String reservationStatus;
  @SerializedName("operation_status")
  private String operationStatus;

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

  public BgpEntity getBgp() {
    return bgp;
  }

  public void setBgp(BgpEntity bgp) {
    this.bgp = bgp;
  }

  public OspfEntity getOspf() {
    return ospf;
  }

  public void setOspf(OspfEntity ospf) {
    this.ospf = ospf;
  }

  public List<StaticRouteEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<StaticRouteEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  public VrrpEntity getVrrp() {
    return vrrp;
  }

  public void setVrrp(VrrpEntity vrrp) {
    this.vrrp = vrrp;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReservationStatus() {
    return reservationStatus;
  }

  public void setReservationStatus(String reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public String getOperationStatus() {
    return operationStatus;
  }

  public void setOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }

  public List<String> getSupportProtocolList() {
    return supportProtocolList;
  }

  public void setSupportProtocolList(List<String> supportProtocolList) {
    this.supportProtocolList = supportProtocolList;
  }

  public ActiveStatus getStatusEnum() {
    return ActiveStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(ActiveStatus status) {
    this.status = status.getMessage();
  }

  public ReserveStatus getReservationStatusEnum() {
    return ReserveStatus.getEnumFromMessage(reservationStatus);
  }

  public void setReservationStatusEnum(ReserveStatus reservationStatus) {
    this.reservationStatus = reservationStatus.getMessage();
  }

  public InterfaceOperationStatus getOperationStatusEnum() {
    return InterfaceOperationStatus.getEnumFromMessage(operationStatus);
  }

  public void setOperationStatusEnum(InterfaceOperationStatus operationStatus) {
    this.operationStatus = operationStatus.getMessage();
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
