
package msf.mfcfc.common.data;

public class EsiIdPK {

  private Integer swClusterId1;

  private Integer swClusterId2;

  public EsiIdPK() {
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
    if (!(other instanceof EsiIdPK)) {
      return false;
    }
    EsiIdPK castOther = (EsiIdPK) other;
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
}
