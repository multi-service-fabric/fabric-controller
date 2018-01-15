package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class VlanIfUpdateOptionEcEntity {
  
  @SerializedName("l3vlan_option")
  private VlanIfL3VlanOptionEcEntity l3vlanOption;

  
  @SerializedName("l2vlan_option")
  private VlanIfL2VlanOptionEcEntity l2vlanOption;

  
  public VlanIfL3VlanOptionEcEntity getL3vlanOption() {
    return l3vlanOption;
  }

  
  public void setL3vlanOption(VlanIfL3VlanOptionEcEntity l3vlanOption) {
    this.l3vlanOption = l3vlanOption;
  }

  
  public VlanIfL2VlanOptionEcEntity getL2vlanOption() {
    return l2vlanOption;
  }

  
  public void setL2vlanOption(VlanIfL2VlanOptionEcEntity l2vlanOption) {
    this.l2vlanOption = l2vlanOption;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
