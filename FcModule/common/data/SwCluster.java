package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "sw_clusters")
@NamedQuery(name = "SwCluster.findAll", query = "SELECT s FROM SwCluster s")
public class SwCluster implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "sw_cluster_id")
  private String swClusterId;

  @Column(name = "as_num")
  private Integer asNum;

  @Column(name = "ec_control_address")
  private String ecControlAddress;

  @Column(name = "ec_control_port")
  private Integer ecControlPort;

  @Column(name = "interface_start_address")
  private String interfaceStartAddress;

  @Column(name = "loopback_start_address")
  private String loopbackStartAddress;

  @Column(name = "management_address_prefix")
  private Integer managementAddressPrefix;

  @Column(name = "management_start_address")
  private String managementStartAddress;

  @Column(name = "max_leaf_num")
  private Integer maxLeafNum;

  @Column(name = "max_spine_num")
  private Integer maxSpineNum;

  @Column(name = "rp_loopback_address")
  private String rpLoopbackAddress;

  @OneToMany(mappedBy = "swCluster")
  private List<EdgePoint> edgePoints;

  @OneToMany(mappedBy = "swCluster")
  private List<Equipment> equipments;

  @OneToMany(mappedBy = "swCluster", cascade = { CascadeType.ALL })
  private List<Rr> rrs;

  public SwCluster() {
  }

  public String getSwClusterId() {
    return this.swClusterId;
  }

  public void setSwClusterId(String swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getAsNum() {
    return this.asNum;
  }

  public void setAsNum(Integer asNum) {
    this.asNum = asNum;
  }

  public String getEcControlAddress() {
    return this.ecControlAddress;
  }

  public void setEcControlAddress(String ecControlAddress) {
    this.ecControlAddress = ecControlAddress;
  }

  public Integer getEcControlPort() {
    return this.ecControlPort;
  }

  public void setEcControlPort(Integer ecControlPort) {
    this.ecControlPort = ecControlPort;
  }

  public String getInterfaceStartAddress() {
    return this.interfaceStartAddress;
  }

  public void setInterfaceStartAddress(String interfaceStartAddress) {
    this.interfaceStartAddress = interfaceStartAddress;
  }

  public String getLoopbackStartAddress() {
    return this.loopbackStartAddress;
  }

  public void setLoopbackStartAddress(String loopbackStartAddress) {
    this.loopbackStartAddress = loopbackStartAddress;
  }

  public Integer getManagementAddressPrefix() {
    return this.managementAddressPrefix;
  }

  public void setManagementAddressPrefix(Integer managementAddressPrefix) {
    this.managementAddressPrefix = managementAddressPrefix;
  }

  public String getManagementStartAddress() {
    return this.managementStartAddress;
  }

  public void setManagementStartAddress(String managementStartAddress) {
    this.managementStartAddress = managementStartAddress;
  }

  public Integer getMaxLeafNum() {
    return this.maxLeafNum;
  }

  public void setMaxLeafNum(Integer maxLeafNum) {
    this.maxLeafNum = maxLeafNum;
  }

  public Integer getMaxSpineNum() {
    return this.maxSpineNum;
  }

  public void setMaxSpineNum(Integer maxSpineNum) {
    this.maxSpineNum = maxSpineNum;
  }

  public String getRpLoopbackAddress() {
    return this.rpLoopbackAddress;
  }

  public void setRpLoopbackAddress(String rpLoopbackAddress) {
    this.rpLoopbackAddress = rpLoopbackAddress;
  }

  public List<EdgePoint> getEdgePoints() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.edgePoints);
  }

  public void setEdgePoints(List<EdgePoint> edgePoints) {
    this.edgePoints = edgePoints;
  }

  public EdgePoint addEdgePoint(EdgePoint edgePoint) throws MsfException {
    getEdgePoints().add(edgePoint);
    edgePoint.setSwCluster(this);

    return edgePoint;
  }

  public EdgePoint removeEdgePoint(EdgePoint edgePoint) throws MsfException {
    getEdgePoints().remove(edgePoint);
    edgePoint.setSwCluster(null);

    return edgePoint;
  }

  public List<Equipment> getEquipments() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.equipments);
  }

  public void setEquipments(List<Equipment> equipments) {
    this.equipments = equipments;
  }

  public Equipment addEquipment(Equipment equipment) throws MsfException {
    getEquipments().add(equipment);
    equipment.setSwCluster(this);

    return equipment;
  }

  public Equipment removeEquipment(Equipment equipment) throws MsfException {
    getEquipments().remove(equipment);
    equipment.setSwCluster(null);

    return equipment;
  }

  public List<Rr> getRrs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.rrs);
  }

  public void setRrs(List<Rr> rrs) {
    this.rrs = rrs;
  }

  public Rr addRr(Rr rr) throws MsfException {
    getRrs().add(rr);
    rr.setSwCluster(this);

    return rr;
  }

  public Rr removeRr(Rr rr) throws MsfException {
    getRrs().remove(rr);
    rr.setSwCluster(null);

    return rr;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "edgePoints", "equipments", "rrs" })
        .toString();
  }

}