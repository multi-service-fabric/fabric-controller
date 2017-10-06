package msf.fc.common.data;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "lag_ifs")
@NamedQuery(name = "LagIf.findAll", query = "SELECT l FROM LagIf l")
public class LagIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lag_if_info_id")
  private Long lagIfInfoId;

  @Column(name = "ipv4_address")
  private String ipv4Address;

  @Column(name = "lag_if_id")
  private Integer lagIfId;

  @Column(name = "minimum_links")
  private Integer minimumLinks;

  @Column(name = "opposite_lag_if_id")
  private Integer oppositeLagIfId;

  private String speed;

  @OneToMany(mappedBy = "lagIf", cascade = { CascadeType.ALL })
  private List<InternalLinkIf> internalLinkIfs;

  @OneToMany(mappedBy = "lagIf", cascade = { CascadeType.REMOVE })
  private List<LagConstruction> lagConstructions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_info_id")
  private Node node;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "opposite_node_info_id")
  private Node oppositeNode;

  public LagIf() {
  }

  public Long getLagIfInfoId() {
    return this.lagIfInfoId;
  }

  public void setLagIfInfoId(Long lagIfInfoId) {
    this.lagIfInfoId = lagIfInfoId;
  }

  public String getIpv4Address() {
    return this.ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public Integer getLagIfId() {
    return this.lagIfId;
  }

  public void setLagIfId(Integer lagIfId) {
    this.lagIfId = lagIfId;
  }

  public Integer getMinimumLinks() {
    return this.minimumLinks;
  }

  public void setMinimumLinks(Integer minimumLinks) {
    this.minimumLinks = minimumLinks;
  }

  public Integer getOppositeLagIfId() {
    return this.oppositeLagIfId;
  }

  public void setOppositeLagIfId(Integer oppositeLagIfId) {
    this.oppositeLagIfId = oppositeLagIfId;
  }

  public String getSpeed() {
    return this.speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public InternalLinkIf getInternalLinkIf() throws MsfException {
    this.internalLinkIfs = SessionWrapper.getLazyLoadData(this.internalLinkIfs);
    return CollectionUtils.isNotEmpty(this.internalLinkIfs) ? this.internalLinkIfs.get(0) : null;
  }

  public void setInternalLinkIf(InternalLinkIf internalLinkIf) {
    internalLinkIfs = new ArrayList<>();
    this.internalLinkIfs.add(internalLinkIf);
  }

  public List<LagConstruction> getLagConstructions() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagConstructions);
  }

  public void setLagConstructions(List<LagConstruction> lagConstructions) {
    this.lagConstructions = lagConstructions;
  }

  public LagConstruction addLagConstruction(LagConstruction lagConstruction) throws MsfException {
    getLagConstructions().add(lagConstruction);
    lagConstruction.setLagIf(this);

    return lagConstruction;
  }

  public LagConstruction removeLagConstruction(LagConstruction lagConstruction) throws MsfException {
    getLagConstructions().remove(lagConstruction);
    lagConstruction.setLagIf(null);

    return lagConstruction;
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
        .setExcludeFieldNames(new String[] { "internalLinkIfs", "lagConstructions", "node", "oppositeNode" })
        .toString();
  }

}