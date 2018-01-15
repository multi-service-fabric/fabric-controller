
package msf.fc.common.data;

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
@NamedQuery(name = "FcCpId.findAll", query = "SELECT f FROM FcCpId f")
public class FcCpId implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcCpIdPK id;

  @Column(name = "next_id")
  private Integer nextId;

  public FcCpId() {
  }

  public FcCpId(CpId cpId) {
    setCommonEntity(cpId);
  }

  public FcCpIdPK getId() {
    return this.id;
  }

  public void setId(FcCpIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  public void setCommonEntity(CpId cpId) {
    FcCpIdPK fcCpIdPk = new FcCpIdPK();
    fcCpIdPk.setCommonEntity(cpId.getId());
    setId(fcCpIdPk);
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