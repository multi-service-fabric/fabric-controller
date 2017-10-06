package msf.fc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class CapabilityEntity {

  @SerializedName("l2vpn")
  private Boolean l2Vpn;

  @SerializedName("l3vpn")
  private Boolean l3Vpn;

  public Boolean isL2Vpn() {
    return l2Vpn;
  }

  public void setL2Vpn(Boolean l2Vpn) {
    this.l2Vpn = l2Vpn;
  }

  public Boolean isL3Vpn() {
    return l3Vpn;
  }

  public void setL3Vpn(Boolean l3Vpn) {
    this.l3Vpn = l3Vpn;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
