
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcLagIfFilterInfoPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "lag_if_info_id", insertable = false, updatable = false)
  private Long lagIfInfoId;

  @Column(name = "term_id")
  private String termId;

  public FcLagIfFilterInfoPK() {
  }

  public Long getLagIfInfoId() {
    return this.lagIfInfoId;
  }

  public void setLagIfInfoId(Long lagIfInfoId) {
    this.lagIfInfoId = lagIfInfoId;
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
    if (!(other instanceof FcLagIfFilterInfoPK)) {
      return false;
    }
    FcLagIfFilterInfoPK castOther = (FcLagIfFilterInfoPK) other;
    return this.lagIfInfoId.equals(castOther.lagIfInfoId) && this.termId.equals(castOther.termId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.lagIfInfoId.hashCode();
    hash = hash * prime + this.termId.hashCode();

    return hash;
  }
}
