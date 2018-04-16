
package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VlanIfSetValueEcEntity {

  @SerializedName("inflow_shaping_rate")
  private Float inflowShapingRate;

  @SerializedName("outflow_shaping_rate")
  private Float outflowShapingRate;

  @SerializedName("remark_menu")
  private String remarkMenu;

  @SerializedName("egress_menu")
  private String egressMenu;

  public Float getInflowShapingRate() {
    return inflowShapingRate;
  }

  public void setInflowShapingRate(Float inflowShapingRate) {
    this.inflowShapingRate = inflowShapingRate;
  }

  public Float getOutflowShapingRate() {
    return outflowShapingRate;
  }

  public void setOutflowShapingRate(Float outflowShapingRate) {
    this.outflowShapingRate = outflowShapingRate;
  }

  public String getRemarkMenu() {
    return remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  public String getEgressMenu() {
    return egressMenu;
  }

  public void setEgressMenu(String egressMenu) {
    this.egressMenu = egressMenu;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
