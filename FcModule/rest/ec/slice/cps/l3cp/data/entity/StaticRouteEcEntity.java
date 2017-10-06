package msf.fc.rest.ec.slice.cps.l3cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.AddressType;

public class StaticRouteEcEntity {
  @SerializedName("address_type")
  private String addrType;
  @SerializedName("address")
  private String addr;
  @SerializedName("prefix")
  private Integer prefix;
  @SerializedName("next_hop")
  private String nextHop;

  public String getAddrType() {
    return addrType;
  }

  public void setAddrType(String addrType) {
    this.addrType = addrType;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
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

  public AddressType getAddrTypeEnum() {
    return AddressType.getEnumFromMessage(addrType);
  }

  public void setAddrTypeEnum(AddressType addrType) {
    this.addrType = addrType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
