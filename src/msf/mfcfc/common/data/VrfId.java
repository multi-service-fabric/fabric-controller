
package msf.mfcfc.common.data;

import java.io.Serializable;

public class VrfId implements Serializable {

  private Integer layerType;

  private Integer nextId;

  public VrfId() {
  }

  public Integer getLayerType() {
    return this.layerType;
  }

  public void setLayerType(Integer layerType) {
    this.layerType = layerType;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

}
