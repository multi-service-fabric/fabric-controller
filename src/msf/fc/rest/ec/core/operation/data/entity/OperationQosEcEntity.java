
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationQosEcEntity {

  @SerializedName("remark_menu")
  private String remarkMenu;

  @SerializedName("inflow_shaping_rate")
  private Float inflowShapingRate;

  @SerializedName("outflow_shaping_rate")
  private Float outflowShapingRate;

  @SerializedName("egress_queue")
  private String egressQueue;

  public String getRemarkMenu() {
    return remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

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

  public String getEgressQueue() {
    return egressQueue;
  }

  public void setEgressQueue(String egressQueue) {
    this.egressQueue = egressQueue;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
