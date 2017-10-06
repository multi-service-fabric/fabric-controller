package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2VpnEcEntity {


  @SerializedName("pim")
  private PimEcEntity pim;

  public PimEcEntity getPim() {
    return pim;
  }

  public void setPim(PimEcEntity pim) {
    this.pim = pim;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
