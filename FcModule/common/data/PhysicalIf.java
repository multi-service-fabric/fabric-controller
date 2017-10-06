package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "physical_ifs")
@NamedQuery(name = "PhysicalIf.findAll", query = "SELECT p FROM PhysicalIf p")
public class PhysicalIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "physical_if_info_id")
  private Long physicalIfInfoId;

  @Column(name = "opposite_physical_if_id")
  private String oppositePhysicalIfId;

  @Column(name = "physical_if_id")
  private String physicalIfId;

  @Column(name = "physical_port_flag")
  private Boolean physicalPortFlag;

  private String speed;

  @OneToOne(mappedBy = "physicalIf", fetch = FetchType.LAZY)
  private LagConstruction lagConstruction;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_info_id")
  private Node node;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "opposite_node_info_id")
  private Node oppositeNode;

  public PhysicalIf() {
  }

  public Long getPhysicalIfInfoId() {
    return this.physicalIfInfoId;
  }

  public void setPhysicalIfInfoId(Long physicalIfInfoId) {
    this.physicalIfInfoId = physicalIfInfoId;
  }

  public String getOppositePhysicalIfId() {
    return this.oppositePhysicalIfId;
  }

  public void setOppositePhysicalIfId(String oppositePhysicalIfId) {
    this.oppositePhysicalIfId = oppositePhysicalIfId;
  }

  public String getPhysicalIfId() {
    return this.physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public Boolean getPhysicalPortFlag() {
    return this.physicalPortFlag;
  }

  public void setPhysicalPortFlag(Boolean physicalPortFlag) {
    this.physicalPortFlag = physicalPortFlag;
  }

  public String getSpeed() {
    return this.speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public LagConstruction getLagConstruction() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagConstruction);
  }

  public void setLagConstruction(LagConstruction lagConstruction) {
    this.lagConstruction = lagConstruction;
  }

  public Node getNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.node);
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public Node getOppositeNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.oppositeNode);
  }

  public void setOppositeNode(Node oppositeNode) {
    this.oppositeNode = oppositeNode;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "lagConstruction", "node", "oppositeNode" })
        .toString();
  }

}