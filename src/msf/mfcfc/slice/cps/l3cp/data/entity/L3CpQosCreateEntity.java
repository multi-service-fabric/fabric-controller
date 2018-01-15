
package msf.mfcfc.slice.cps.l3cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class L3CpQosCreateEntity {

  
  @SerializedName("ingress_shaping_rate")
  private Float ingressShapingRate;

  
  @SerializedName("egress_shaping_rate")
  private Float egressShapingRate;

  
  @SerializedName("egress_queue_menu")
  private String egressQueueMenu;

  
  public Float getIngressShapingRate() {
    return ingressShapingRate;
  }

  
  public void setIngressShapingRate(Float ingressShapingRate) {
    this.ingressShapingRate = ingressShapingRate;
  }

  
  public Float getEgressShapingRate() {
    return egressShapingRate;
  }

  
  public void setEgressShapingRate(Float egressShapingRate) {
    this.egressShapingRate = egressShapingRate;
  }

  
  public String getEgressQueueMenu() {
    return egressQueueMenu;
  }

  
  public void setEgressQueueMenu(String egressQueueMenu) {
    this.egressQueueMenu = egressQueueMenu;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
