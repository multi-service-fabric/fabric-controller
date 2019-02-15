
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "physical_if_filter_info")
@NamedQuery(name = "FcPhysicalIfFilterInfo.findAll", query = "SELECT f FROM FcPhysicalIfFilterInfo f")
public class FcPhysicalIfFilterInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcPhysicalIfFilterInfoPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "physical_if_info_id", insertable = false, updatable = false)
  private FcPhysicalIf physicalIf;

  public FcPhysicalIfFilterInfo() {
  }

  public FcPhysicalIfFilterInfoPK getId() {
    return this.id;
  }

  public void setId(FcPhysicalIfFilterInfoPK id) {
    this.id = id;
  }

  public FcPhysicalIf getPhysicalIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalIf);
  }

  public void setPhysicalIf(FcPhysicalIf physicalIf) {
    this.physicalIf = physicalIf;
  }

}
