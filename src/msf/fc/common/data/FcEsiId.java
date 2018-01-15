
package msf.fc.common.data;

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
@NamedQuery(name = "FcEsiId.findAll", query = "SELECT f FROM FcEsiId f")
public class FcEsiId implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcEsiIdPK id;

  @Column(name = "next_id")
  private Integer nextId;

  public FcEsiId() {
  }

  public FcEsiId(EsiId esiId) {
    setCommonEntity(esiId);
  }

  public FcEsiIdPK getId() {
    return this.id;
  }

  public void setId(FcEsiIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  public void setCommonEntity(EsiId esiId) {
    FcEsiIdPK fcEsiIdPk = new FcEsiIdPK();
    fcEsiIdPk.setCommonEntity(esiId.getId());
    setId(fcEsiIdPk);
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