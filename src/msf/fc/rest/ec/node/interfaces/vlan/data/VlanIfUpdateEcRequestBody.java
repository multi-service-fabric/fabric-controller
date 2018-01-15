package msf.fc.rest.ec.node.interfaces.vlan.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfUpdateOptionEcEntity;


public class VlanIfUpdateEcRequestBody {

  
  @SerializedName("update_option")
  private VlanIfUpdateOptionEcEntity updateOption;

  
  public VlanIfUpdateOptionEcEntity getUpdateOption() {
    return updateOption;
  }

  
  public void setUpdateOption(VlanIfUpdateOptionEcEntity updateOption) {
    this.updateOption = updateOption;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
