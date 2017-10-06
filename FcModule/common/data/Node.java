package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.PlaneBelongsTo;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.constant.VpnType;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "nodes")
@NamedQuery(name = "Node.findAll", query = "SELECT n FROM Node n")
public class Node implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "node_info_id")
  private Long nodeInfoId;

  @Column(name = "host_name")
  private String hostName;

  @Column(name = "mac_addr")
  private String macAddr;

  @Column(name = "management_if_address")
  private String managementIfAddress;

  @Column(name = "node_id")
  private Integer nodeId;

  @Column(name = "node_type")
  private Integer nodeType;

  @Column(name = "ntp_server_address")
  private String ntpServerAddress;

  private String passward;

  private Integer plane;

  private Boolean provisioning;

  @Column(name = "provisioning_status")
  private Integer provisioningStatus;

  @Column(name = "router_id")
  private String routerId;

  @Column(name = "rp_flag")
  private Boolean rpFlag;

  @Column(name = "snmp_community")
  private String snmpCommunity;

  private String username;

  @Column(name = "vpn_type")
  private String vpnType;

  @OneToMany(mappedBy = "node", cascade = { CascadeType.REMOVE })
  private List<LagIf> lagIfs;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "equipment_type_id", referencedColumnName = "equipment_type_id"),
      @JoinColumn(name = "sw_cluster_id", referencedColumnName = "sw_cluster_id") })
  private Equipment equipment;

  @OneToMany(mappedBy = "node", cascade = { CascadeType.ALL })
  private List<PhysicalIf> physicalIfs;

  @ManyToMany(mappedBy = "nodes")
  private List<Rr> rrs;

  public Node() {
  }

  public Long getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Long nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public String getHostName() {
    return this.hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getMacAddr() {
    return this.macAddr;
  }

  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
  }

  public String getManagementIfAddress() {
    return this.managementIfAddress;
  }

  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  public Integer getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Integer getNodeType() {
    return this.nodeType;
  }

  public void setNodeType(Integer nodeType) {
    this.nodeType = nodeType;
  }

  public NodeType getNodeTypeEnum() {
    return NodeType.getEnumFromCode(this.nodeType);
  }

  public void setNodeTypeEnum(NodeType nodeType) {
    this.nodeType = nodeType.getCode();
  }

  public String getNtpServerAddress() {
    return this.ntpServerAddress;
  }

  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  public String getPassward() {
    return this.passward;
  }

  public void setPassward(String passward) {
    this.passward = passward;
  }

  public Integer getPlane() {
    return this.plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public PlaneBelongsTo getPlaneEnum() {
    return this.plane != null ? PlaneBelongsTo.getEnumFromCode(this.plane) : null;
  }

  public void setPlaneEnum(PlaneBelongsTo plane) {
    this.plane = plane.getCode();
  }

  public Boolean getProvisioning() {
    return this.provisioning;
  }

  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
  }

  public Integer getProvisioningStatus() {
    return this.provisioningStatus;
  }

  public void setProvisioningStatus(Integer provisioningStatus) {
    this.provisioningStatus = provisioningStatus;
  }

  public ProvisioningStatus getProvisioningStatusEnum() {
    return ProvisioningStatus.getEnumFromCode(this.provisioningStatus);
  }

  public void setProvisioningStatusEnum(ProvisioningStatus provisioningStatus) {
    this.provisioningStatus = provisioningStatus.getCode();
  }

  public String getRouterId() {
    return this.routerId;
  }

  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  public Boolean getRpFlag() {
    return this.rpFlag;
  }

  public void setRpFlag(Boolean rpFlag) {
    this.rpFlag = rpFlag;
  }

  public String getSnmpCommunity() {
    return this.snmpCommunity;
  }

  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getVpnType() {
    return this.vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  public VpnType getVpnTypeEnum() {
    return VpnType.getEnumFromCode(this.vpnType);
  }

  public void setVpnTypeEnum(VpnType vpnType) {
    this.vpnType = vpnType.getCode();
  }

  public List<LagIf> getLagIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIfs);
  }

  public void setLagIfs(List<LagIf> lagIfs) {
    this.lagIfs = lagIfs;
  }

  public LagIf addLagIf(LagIf lagIf) throws MsfException {
    getLagIfs().add(lagIf);
    lagIf.setNode(this);

    return lagIf;
  }

  public LagIf removeLagIf(LagIf lagIf) throws MsfException {
    getLagIfs().remove(lagIf);
    lagIf.setNode(null);

    return lagIf;
  }

  public Equipment getEquipment() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.equipment);
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  public List<PhysicalIf> getPhysicalIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalIfs);
  }

  public void setPhysicalIfs(List<PhysicalIf> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  public PhysicalIf addPhysicalIf(PhysicalIf physicalIf) throws MsfException {
    getPhysicalIfs().add(physicalIf);
    physicalIf.setNode(this);

    return physicalIf;
  }

  public PhysicalIf removePhysicalIf(PhysicalIf physicalIf) throws MsfException {
    getPhysicalIfs().remove(physicalIf);
    physicalIf.setNode(null);

    return physicalIf;
  }

  public List<Rr> getRrs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.rrs);
  }

  public void setRrs(List<Rr> rrs) {
    this.rrs = rrs;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "lagIfs", "equipment", "physicalIfs", "rrs" })
        .toString();
  }

}