
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "nodes")
@NamedQuery(name = "FcNode.findAll", query = "SELECT f FROM FcNode f")
public class FcNode implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "node_info_id")
  private Long nodeInfoId;

  @Column(name = "ec_node_id")
  private Integer ecNodeId;

  @Column(name = "node_id")
  private Integer nodeId;

  @Column(name = "node_type")
  private Integer nodeType;

  @OneToMany(mappedBy = "node", cascade = CascadeType.REMOVE)
  private List<FcBreakoutIf> breakoutIfs;

  @OneToMany(mappedBy = "node", cascade = CascadeType.REMOVE)
  private List<FcLagIf> lagIfs;

  @OneToOne(mappedBy = "node", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private FcLeafNode leafNode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipment_type_id")
  private FcEquipment equipment;

  @OneToMany(mappedBy = "node", cascade = CascadeType.REMOVE)
  private List<FcPhysicalIf> physicalIfs;

  public FcNode() {
  }

  public Long getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Long nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public Integer getEcNodeId() {
    return this.ecNodeId;
  }

  public void setEcNodeId(Integer ecNodeId) {
    this.ecNodeId = ecNodeId;
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

  public List<FcBreakoutIf> getBreakoutIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.breakoutIfs);
  }

  public void setBreakoutIfs(List<FcBreakoutIf> breakoutIfs) {
    this.breakoutIfs = breakoutIfs;
  }

  public FcBreakoutIf addBreakoutIf(FcBreakoutIf breakoutIf) throws MsfException {
    getBreakoutIfs().add(breakoutIf);
    breakoutIf.setNode(this);

    return breakoutIf;
  }

  public FcBreakoutIf removeBreakoutIf(FcBreakoutIf breakoutIf) throws MsfException {
    getBreakoutIfs().remove(breakoutIf);
    breakoutIf.setNode(null);

    return breakoutIf;
  }

  public List<FcLagIf> getLagIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIfs);
  }

  public void setLagIfs(List<FcLagIf> lagIfs) {
    this.lagIfs = lagIfs;
  }

  public FcLagIf addLagIf(FcLagIf lagIf) throws MsfException {
    getLagIfs().add(lagIf);
    lagIf.setNode(this);

    return lagIf;
  }

  public FcLagIf removeLagIf(FcLagIf lagIf) throws MsfException {
    getLagIfs().remove(lagIf);
    lagIf.setNode(null);

    return lagIf;
  }

  public FcLeafNode getLeafNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.leafNode);
  }

  public void setLeafNode(FcLeafNode leafNode) {
    this.leafNode = leafNode;
  }

  public FcEquipment getEquipment() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.equipment);
  }

  public void setEquipment(FcEquipment equipment) {
    this.equipment = equipment;
  }

  public List<FcPhysicalIf> getPhysicalIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalIfs);
  }

  public void setPhysicalIfs(List<FcPhysicalIf> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  public FcPhysicalIf addPhysicalIf(FcPhysicalIf physicalIf) throws MsfException {
    getPhysicalIfs().add(physicalIf);
    physicalIf.setNode(this);

    return physicalIf;
  }

  public FcPhysicalIf removePhysicalIf(FcPhysicalIf physicalIf) throws MsfException {
    getPhysicalIfs().remove(physicalIf);
    physicalIf.setNode(null);

    return physicalIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "breakoutIfs", "lagIfs", "leafNode", "equipment", "physicalIfs" })
        .toString();
  }

}