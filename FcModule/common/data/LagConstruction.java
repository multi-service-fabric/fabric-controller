package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "lag_constructions")
@NamedQuery(name = "LagConstruction.findAll", query = "SELECT l FROM LagConstruction l")
public class LagConstruction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "physical_if_info_id")
  private Long physicalIfInfoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lag_if_info_id")
  private LagIf lagIf;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "physical_if_info_id")
  private PhysicalIf physicalIf;

  public LagConstruction() {
  }

  public Long getPhysicalIfInfoId() {
    return this.physicalIfInfoId;
  }

  public void setPhysicalIfInfoId(Long physicalIfInfoId) {
    this.physicalIfInfoId = physicalIfInfoId;
  }

  public LagIf getLagIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIf);
  }

  public void setLagIf(LagIf lagIf) {
    this.lagIf = lagIf;
  }

  public PhysicalIf getPhysicalIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalIf);
  }

  public void setPhysicalIf(PhysicalIf physicalIf) {
    this.physicalIf = physicalIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "lagIf", "physicalIf" })
        .toString();
  }

}