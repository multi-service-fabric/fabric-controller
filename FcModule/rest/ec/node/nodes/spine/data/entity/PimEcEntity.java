package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PimEcEntity {
  @SerializedName("other_rp_address")
  private String otherRpAddress;

  @SerializedName("self_rp_address")
  private String rpAddress;

  public String getOtherRpAddress() {
    return otherRpAddress;
  }

  public void setOtherRpAddress(String otherRpAddress) {
    this.otherRpAddress = otherRpAddress;
  }

  public String getRpAddress() {
    return rpAddress;
  }

  public void setRpAddress(String rpAddress) {
    this.rpAddress = rpAddress;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
