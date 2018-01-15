package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class VlanIfStaticRouteEcEntity {
  
  @SerializedName("address_type")
  private String addressType;

  
  @SerializedName("address")
  private String address;

  
  @SerializedName("prefix")
  private Integer prefix;

  
  @SerializedName("next_hop")
  private String nextHop;

  
  public String getAddressType() {
    return addressType;
  }

  
  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  
  public String getAddress() {
    return address;
  }

  
  public void setAddress(String address) {
    this.address = address;
  }

  
  public Integer getPrefix() {
    return prefix;
  }

  
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  
  public String getNextHop() {
    return nextHop;
  }

  
  public void setNextHop(String nextHop) {
    this.nextHop = nextHop;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
