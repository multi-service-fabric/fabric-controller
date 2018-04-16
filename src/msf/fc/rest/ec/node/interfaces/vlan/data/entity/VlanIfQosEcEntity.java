
package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VlanIfQosEcEntity {

  @SerializedName("capability")
  private VlanIfCapabilityEcEntity capability;

  @SerializedName("set_value")
  private VlanIfSetValueEcEntity setValue;

  public VlanIfCapabilityEcEntity getCapability() {
    return capability;
  }

  public void setCapability(VlanIfCapabilityEcEntity capability) {
    this.capability = capability;
  }

  public VlanIfSetValueEcEntity getSetValue() {
    return setValue;
  }

  public void setSetValue(VlanIfSetValueEcEntity setValue) {
    this.setValue = setValue;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
