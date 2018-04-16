
package msf.mfcfc.common.data;

public class CpId {

  private CpIdPK id;

  private Integer nextId;

  public CpId() {
  }

  public CpIdPK getId() {
    return this.id;
  }

  public void setId(CpIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

}
