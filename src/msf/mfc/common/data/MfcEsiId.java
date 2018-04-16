
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.data.EsiId;

@Entity
@Table(name = "esi_ids")
@NamedQuery(name = "MfcEsiId.findAll", query = "SELECT m FROM MfcEsiId m")
public class MfcEsiId implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private MfcEsiIdPK id;

  @Column(name = "next_id")
  private Integer nextId;

  public MfcEsiId() {
  }

  public MfcEsiId(EsiId esiId) {
    setCommonEntity(esiId);
  }

  public MfcEsiIdPK getId() {
    return this.id;
  }

  public void setId(MfcEsiIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  public void setCommonEntity(EsiId esiId) {
    MfcEsiIdPK mfcEsiIdPk = new MfcEsiIdPK();
    mfcEsiIdPk.setCommonEntity(esiId.getId());
    setId(mfcEsiIdPk);
    setNextId(esiId.getNextId());
  }

  public EsiId getCommonEntity() {
    EsiId esiId = new EsiId();
    esiId.setId(getId().getCommonEntity());
    esiId.setNextId(getNextId());
    return esiId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
