
package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VlanIfIrbEcEntity {

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;

  @SerializedName("virtual_gateway_address")
  private String virtualGatewayAddress;

  @SerializedName("virtual_gateway_prefix")
  private Integer virtualGatewayPrefix;

  public String getIpv4Address() {
    return ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public Integer getIpv4Prefix() {
    return ipv4Prefix;
  }

  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  public String getVirtualGatewayAddress() {
    return virtualGatewayAddress;
  }

  public void setVirtualGatewayAddress(String virtualGatewayAddress) {
    this.virtualGatewayAddress = virtualGatewayAddress;
  }

  public Integer getVirtualGatewayPrefix() {
    return virtualGatewayPrefix;
  }

  public void setVirtualGatewayPrefix(Integer virtualGatewayPrefix) {
    this.virtualGatewayPrefix = virtualGatewayPrefix;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
