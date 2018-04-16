
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import msf.mfcfc.common.data.EsiIdPK;

@Embeddable
public class FcEsiIdPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "sw_cluster_id_1")
  private Integer swClusterId1;

  @Column(name = "sw_cluster_id_2")
  private Integer swClusterId2;

  public FcEsiIdPK() {
  }

  public FcEsiIdPK(EsiIdPK esiIdPk) {
    setCommonEntity(esiIdPk);
  }

  public Integer getSwClusterId1() {
    return this.swClusterId1;
  }

  public void setSwClusterId1(Integer swClusterId1) {
    this.swClusterId1 = swClusterId1;
  }

  public Integer getSwClusterId2() {
    return this.swClusterId2;
  }

  public void setSwClusterId2(Integer swClusterId2) {
    this.swClusterId2 = swClusterId2;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof FcEsiIdPK)) {
      return false;
    }
    FcEsiIdPK castOther = (FcEsiIdPK) other;
    return this.swClusterId1.equals(castOther.swClusterId1) && this.swClusterId2.equals(castOther.swClusterId2);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.swClusterId1.hashCode();
    hash = hash * prime + this.swClusterId2.hashCode();

    return hash;
  }

  public void setCommonEntity(EsiIdPK esiIdPk) {
    setSwClusterId1(esiIdPk.getSwClusterId1());
    setSwClusterId2(esiIdPk.getSwClusterId2());
  }

  public EsiIdPK getCommonEntity() {
    EsiIdPK esiIdPk = new EsiIdPK();
    esiIdPk.setSwClusterId1(getSwClusterId1());
    esiIdPk.setSwClusterId2(getSwClusterId2());

    return esiIdPk;
  }
}
