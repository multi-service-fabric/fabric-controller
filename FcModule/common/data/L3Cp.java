package msf.fc.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "l3_cps")
@NamedQuery(name = "L3Cp.findAll", query = "SELECT l FROM L3Cp l")
public class L3Cp implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private L3CpPK id;

  @Column(name = "ipv4_address")
  private String ipv4Address;

  @Column(name = "ipv4_prefix")
  private Integer ipv4Prefix;

  @Column(name = "ipv6_address")
  private String ipv6Address;

  @Column(name = "ipv6_prefix")
  private Integer ipv6Prefix;

  private Integer mtu;

  @Column(name = "operation_status")
  private Integer operationStatus;

  @Column(name = "reservation_status")
  private Integer reservationStatus;

  private Integer status;

  @Column(name = "vlan_id")
  private Integer vlanId;

  @OneToMany(mappedBy = "l3Cp", cascade = { CascadeType.ALL })
  private List<L3CpBgpOption> l3CpBgpOptions;

  @OneToMany(mappedBy = "l3Cp", cascade = { CascadeType.ALL })
  private List<L3CpOspfOption> l3CpOspfOptions;

  @OneToMany(mappedBy = "l3Cp", cascade = { CascadeType.REMOVE })
  private List<L3CpStaticRouteOption> l3CpStaticRouteOptions;

  @OneToMany(mappedBy = "l3Cp", cascade = { CascadeType.ALL })
  private List<L3CpVrrpOption> l3CpVrrpOptions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "edge_point_id", referencedColumnName = "edge_point_id"),
      @JoinColumn(name = "sw_cluster_id", referencedColumnName = "sw_cluster_id") })
  private EdgePoint edgePoint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id", insertable = false, updatable = false)
  private L3Slice l3Slice;

  public L3Cp() {
  }

  public L3CpPK getId() {
    return this.id;
  }

  public void setId(L3CpPK id) {
    this.id = id;
  }

  public String getIpv4Address() {
    return this.ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public Integer getIpv4Prefix() {
    return this.ipv4Prefix;
  }

  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  public String getIpv6Address() {
    return this.ipv6Address;
  }

  public void setIpv6Address(String ipv6Address) {
    this.ipv6Address = ipv6Address;
  }

  public Integer getIpv6Prefix() {
    return this.ipv6Prefix;
  }

  public void setIpv6Prefix(Integer ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }

  public Integer getMtu() {
    return this.mtu;
  }

  public void setMtu(Integer mtu) {
    this.mtu = mtu;
  }

  public Integer getOperationStatus() {
    return this.operationStatus;
  }

  public void setOperationStatus(Integer operationStatus) {
    this.operationStatus = operationStatus;
  }

  public InterfaceOperationStatus getOperationStatusEnum() {
    return InterfaceOperationStatus.getEnumFromCode(this.operationStatus);
  }

  public void setOperationStatusEnum(InterfaceOperationStatus operationStatus) {
    this.operationStatus = operationStatus.getCode();
  }

  public Integer getReservationStatus() {
    return this.reservationStatus;
  }

  public void setReservationStatus(Integer reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public ReserveStatus getReservationStatusEnum() {
    return ReserveStatus.getEnumFromCode(this.reservationStatus);
  }

  public void setReservationStatusEnum(ReserveStatus reservationStatus) {
    this.reservationStatus = reservationStatus.getCode();
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ActiveStatus getStatusEnum() {
    return ActiveStatus.getEnumFromCode(this.status);
  }

  public void setStatusEnum(ActiveStatus status) {
    this.status = status.getCode();
  }

  public Integer getVlanId() {
    return this.vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  public L3CpBgpOption getL3CpBgpOption() throws MsfException {
    this.l3CpBgpOptions = SessionWrapper.getLazyLoadData(this.l3CpBgpOptions);
    return CollectionUtils.isNotEmpty(this.l3CpBgpOptions) ? this.l3CpBgpOptions.get(0) : null;
  }

  public void setL3CpBgpOption(L3CpBgpOption l3CpBgpOption) {
    l3CpBgpOptions = new ArrayList<>();
    this.l3CpBgpOptions.add(l3CpBgpOption);
  }

  public L3CpOspfOption getL3CpOspfOption() throws MsfException {
    this.l3CpOspfOptions = SessionWrapper.getLazyLoadData(this.l3CpOspfOptions);
    return CollectionUtils.isNotEmpty(this.l3CpOspfOptions) ? this.l3CpOspfOptions.get(0) : null;
  }

  public void setL3CpOspfOption(L3CpOspfOption l3CpOspfOption) {
    l3CpOspfOptions = new ArrayList<>();
    this.l3CpOspfOptions.add(l3CpOspfOption);
  }

  public List<L3CpStaticRouteOption> getL3CpStaticRouteOptions() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3CpStaticRouteOptions);
  }

  public void setL3CpStaticRouteOptions(List<L3CpStaticRouteOption> l3CpStaticRouteOptions) {
    this.l3CpStaticRouteOptions = l3CpStaticRouteOptions;
  }

  public L3CpStaticRouteOption addL3CpStaticRouteOption(L3CpStaticRouteOption l3CpStaticRouteOption)
      throws MsfException {
    getL3CpStaticRouteOptions().add(l3CpStaticRouteOption);
    l3CpStaticRouteOption.setL3Cp(this);

    return l3CpStaticRouteOption;
  }

  public L3CpStaticRouteOption removeL3CpStaticRouteOption(L3CpStaticRouteOption l3CpStaticRouteOption)
      throws MsfException {
    getL3CpStaticRouteOptions().remove(l3CpStaticRouteOption);
    l3CpStaticRouteOption.setL3Cp(null);

    return l3CpStaticRouteOption;
  }

  public L3CpVrrpOption getL3CpVrrpOption() throws MsfException {
    this.l3CpVrrpOptions = SessionWrapper.getLazyLoadData(this.l3CpVrrpOptions);
    return CollectionUtils.isNotEmpty(this.l3CpVrrpOptions) ? this.l3CpVrrpOptions.get(0) : null;
  }

  public void setL3CpVrrpOption(L3CpVrrpOption l3CpVrrpOption) {
    l3CpVrrpOptions = new ArrayList<>();
    this.l3CpVrrpOptions.add(l3CpVrrpOption);
  }

  public EdgePoint getEdgePoint() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.edgePoint);
  }

  public void setEdgePoint(EdgePoint edgePoint) {
    this.edgePoint = edgePoint;
  }

  public L3Slice getL3Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Slice);
  }

  public void setL3Slice(L3Slice l3Slice) {
    this.l3Slice = l3Slice;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "l3CpBgpOptions", "l3CpOspfOptions", "l3CpStaticRouteOptions",
            "l3CpVrrpOptions", "edgePoint", "l3Slice" })
        .toString();
  }

}