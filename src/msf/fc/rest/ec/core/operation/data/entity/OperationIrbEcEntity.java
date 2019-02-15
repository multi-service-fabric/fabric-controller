
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationIrbEcEntity {

  @SerializedName("vni")
  private Integer vni;

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;

  @SerializedName("virtual_gateway_address")
  private String virtualGatewayAddress;

  public Integer getVni() {
    return vni;
  }

  public void setVni(Integer vni) {
    this.vni = vni;
  }

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
