
package msf.mfcfc.slice.cps.l2cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2CpIrbEntity {

  @SerializedName("irb_ipv4_address")
  private String irbIpv4Address;

  @SerializedName("vga_ipv4_address")
  private String vgaIpv4Address;

  @SerializedName("ipv4_address_prefix")
  private Integer ipv4AddressPrefix;

  public String getIrbIpv4Address() {
    return irbIpv4Address;
  }

  public void setIrbIpv4Address(String irbIpv4Address) {
    this.irbIpv4Address = irbIpv4Address;
  }

  public String getVgaIpv4Address() {
    return vgaIpv4Address;
  }

  public void setVgaIpv4Address(String vgaIpv4Address) {
    this.vgaIpv4Address = vgaIpv4Address;
  }

  public Integer getIpv4AddressPrefix() {
    return ipv4AddressPrefix;
  }

  public void setIpv4AddressPrefix(Integer ipv4AddressPrefix) {
    this.ipv4AddressPrefix = ipv4AddressPrefix;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
