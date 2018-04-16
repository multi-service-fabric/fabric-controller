
package msf.mfcfc.common.data;

import java.io.Serializable;

public class VlanIfId implements Serializable {

  private Integer nodeInfoId;

  private Integer nextId;

  public VlanIfId() {
  }

  public Integer getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Integer nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

}
