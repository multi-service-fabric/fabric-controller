
package msf.fc.rest.ec.node.equipment.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentCapabilitiesEcEntity {

  @SerializedName("l2vpn")
  private Boolean l2vpn;

  @SerializedName("l3vpn")
  private Boolean l3vpn;

  @SerializedName("evpn")
  private Boolean evpn;

  public Boolean getL2vpn() {
    return l2vpn;
  }

  public void setL2vpn(Boolean l2vpn) {
    this.l2vpn = l2vpn;
  }

  public Boolean getL3vpn() {
    return l3vpn;
  }

  public void setL3vpn(Boolean l3vpn) {
    this.l3vpn = l3vpn;
  }

  public Boolean getEvpn() {
    return evpn;
  }

  public void setEvpn(Boolean evpn) {
    this.evpn = evpn;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
