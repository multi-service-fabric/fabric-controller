package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PimEcEntity {
  @SerializedName("rp_address")
  private String rpAddress;

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
