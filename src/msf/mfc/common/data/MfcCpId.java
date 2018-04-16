
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.data.CpId;

@Entity
@Table(name = "cp_ids")
@NamedQuery(name = "MfcCpId.findAll", query = "SELECT m FROM MfcCpId m")
public class MfcCpId implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private MfcCpIdPK id;

  @Column(name = "next_id")
  private Integer nextId;

  public MfcCpId() {
  }

  public MfcCpId(CpId cpId) {
    setCommonEntity(cpId);
  }

  public MfcCpIdPK getId() {
    return this.id;
  }

  public void setId(MfcCpIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  public void setCommonEntity(CpId cpId) {
    MfcCpIdPK mfcCpIdPk = new MfcCpIdPK();
    mfcCpIdPk.setCommonEntity(cpId.getId());
    setId(mfcCpIdPk);
    setNextId(cpId.getNextId());
  }

  public CpId getCommonEntity() {
    CpId cpId = new CpId();
    cpId.setId(getId().getCommonEntity());
    cpId.setNextId(getNextId());
    return cpId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
