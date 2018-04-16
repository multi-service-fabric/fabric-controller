
package msf.fc.rest.ec.node.interfaces.vlan.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class VlanIfReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("vlan_if")
  private VlanIfEcEntity vlanIf;

  public VlanIfEcEntity getVlanIf() {
    return vlanIf;
  }

  public void setVlanIf(VlanIfEcEntity vlanIf) {
    this.vlanIf = vlanIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
