
package msf.mfcfc.common.data;

public class EsiId {

  private EsiIdPK id;

  private Integer nextId;

  public EsiId() {
  }

  public EsiIdPK getId() {
    return this.id;
  }

  public void setId(EsiIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

}
