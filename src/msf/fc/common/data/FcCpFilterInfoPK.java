
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcCpFilterInfoPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "slice_id", insertable = false, updatable = false)
  private String sliceId;

  @Column(name = "cp_id", insertable = false, updatable = false)
  private String cpId;

  @Column(name = "term_id")
  private String termId;

  public FcCpFilterInfoPK() {
  }

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getCpId() {
    return this.cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
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
    if (!(other instanceof FcCpFilterInfoPK)) {
      return false;
    }
    FcCpFilterInfoPK castOther = (FcCpFilterInfoPK) other;
    return this.sliceId.equals(castOther.sliceId) && this.cpId.equals(castOther.cpId)
        && this.termId.equals(castOther.termId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.sliceId.hashCode();
    hash = hash * prime + this.cpId.hashCode();
    hash = hash * prime + this.termId.hashCode();

    return hash;
  }
}
