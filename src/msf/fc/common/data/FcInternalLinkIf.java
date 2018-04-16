
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "internal_link_ifs")
@NamedQuery(name = "FcInternalLinkIf.findAll", query = "SELECT f FROM FcInternalLinkIf f")
public class FcInternalLinkIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "internal_link_if_id")
  private Integer internalLinkIfId;

  @Column(name = "traffic_threshold")
  private Double trafficThreshold;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "breakout_if_info_id")
  private FcBreakoutIf breakoutIf;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "opposite_internal_link_if_id")
  private FcInternalLinkIf internalLinkIf;

  @OneToMany(mappedBy = "internalLinkIf", cascade = { CascadeType.ALL })
  private List<FcInternalLinkIf> oppositeInternalLinkIfs;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lag_if_id")
  private FcLagIf lagIf;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "physical_if_info_id")
  private FcPhysicalIf physicalIf;

  public FcInternalLinkIf() {
  }

  public Integer getInternalLinkIfId() {
    return this.internalLinkIfId;
  }

  public void setInternalLinkIfId(Integer internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
  }

  public Double getTrafficThreshold() {
    return this.trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  public FcBreakoutIf getBreakoutIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.breakoutIf);
  }

  public void setBreakoutIf(FcBreakoutIf breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  public FcInternalLinkIf getInternalLinkIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.internalLinkIf);
  }

  public void setInternalLinkIf(FcInternalLinkIf internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  public List<FcInternalLinkIf> getOppositeInternalLinkIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.oppositeInternalLinkIfs);
  }

  public void setOppositeInternalLinkIfs(List<FcInternalLinkIf> oppositeInternalLinkIfs) {
    this.oppositeInternalLinkIfs = oppositeInternalLinkIfs;
  }

  public FcInternalLinkIf addOppositeInternalLinkIf(FcInternalLinkIf oppositeInternalLinkIf) throws MsfException {
    getOppositeInternalLinkIfs().add(oppositeInternalLinkIf);
    oppositeInternalLinkIf.setInternalLinkIf(this);

    return oppositeInternalLinkIf;
  }

  public FcInternalLinkIf removeOppositeInternalLinkIf(FcInternalLinkIf oppositeInternalLinkIf) throws MsfException {
    getOppositeInternalLinkIfs().remove(oppositeInternalLinkIf);
    oppositeInternalLinkIf.setInternalLinkIf(null);

    return oppositeInternalLinkIf;
  }

  public FcLagIf getLagIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIf);
  }

  public void setLagIf(FcLagIf lagIf) {
    this.lagIf = lagIf;
  }

  public FcPhysicalIf getPhysicalIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalIf);
  }

  public void setPhysicalIf(FcPhysicalIf physicalIf) {
    this.physicalIf = physicalIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(
            new String[] { "breakoutIf", "internalLinkIf", "oppositeInternalLinkIfs", "lagIf", "physicalIf" })
        .toString();
  }

}
