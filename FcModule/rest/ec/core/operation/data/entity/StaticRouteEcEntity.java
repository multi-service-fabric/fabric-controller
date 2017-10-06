package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.AddressType;

public class StaticRouteEcEntity {
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

  public AddressType getAddressTypeEnum() {
    return AddressType.getEnumFromMessage(addressType);
  }

  public void setAddressTypeEnum(AddressType addressType) {
    this.addressType = addressType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
