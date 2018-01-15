
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcL3CpPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "slice_id", insertable = false, updatable = false)
  private String sliceId;

  @Column(name = "cp_id")
  private String cpId;

  public FcL3CpPK() {
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

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof FcL3CpPK)) {
      return false;
    }
    FcL3CpPK castOther = (FcL3CpPK) other;
    return this.sliceId.equals(castOther.sliceId) && this.cpId.equals(castOther.cpId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.sliceId.hashCode();
    hash = hash * prime + this.cpId.hashCode();

    return hash;
  }
}