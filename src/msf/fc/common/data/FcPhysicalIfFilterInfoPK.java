
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcPhysicalIfFilterInfoPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "physical_if_info_id", insertable = false, updatable = false)
  private Long physicalIfInfoId;

  @Column(name = "term_id")
  private String termId;

  public FcPhysicalIfFilterInfoPK() {
  }

  public Long getPhysicalIfInfoId() {
    return this.physicalIfInfoId;
  }

  public void setPhysicalIfInfoId(Long physicalIfInfoId) {
    this.physicalIfInfoId = physicalIfInfoId;
  }

  public String getTermId() {
    return this.termId;
  }

  public void setTermId(String termId) {
    this.termId = termId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof FcPhysicalIfFilterInfoPK)) {
      return false;
    }
    FcPhysicalIfFilterInfoPK castOther = (FcPhysicalIfFilterInfoPK) other;
    return this.physicalIfInfoId.equals(castOther.physicalIfInfoId) && this.termId.equals(castOther.termId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.physicalIfInfoId.hashCode();
    hash = hash * prime + this.termId.hashCode();

    return hash;
  }
}
