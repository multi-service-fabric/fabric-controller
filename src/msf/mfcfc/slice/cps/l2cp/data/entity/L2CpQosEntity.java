
package msf.mfcfc.slice.cps.l2cp.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class L2CpQosEntity {
  
  @SerializedName("remark")
  private Boolean remark;

  
  @SerializedName("remark_capability")
  private List<String> remarkCapabilityList;

  
  @SerializedName("shaping")
  private Boolean shaping;

  
  @SerializedName("ingress_shaping_rate")
  private Float ingressShapingRate;

  
  @SerializedName("egress_shaping_rate")
  private Float egressShapingRate;

  
  @SerializedName("egress_queue_capability")
  private List<String> egressQueueCapabilityList;

  
  @SerializedName("remark_menu")
  private String remarkMenu;

  
  @SerializedName("egress_queue_menu")
  private String egressQueueMenu;

  
  public Boolean getRemark() {
    return remark;
  }

  
  public void setRemark(Boolean remark) {
    this.remark = remark;
  }

  
  public List<String> getRemarkCapabilityList() {
    return remarkCapabilityList;
  }

  
  public void setRemarkCapabilityList(List<String> remarkCapabilityList) {
    this.remarkCapabilityList = remarkCapabilityList;
  }

  
  public Boolean getShaping() {
    return shaping;
  }

  
  public void setShaping(Boolean shaping) {
    this.shaping = shaping;
  }

  
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

  
  public List<String> getEgressQueueCapabilityList() {
    return egressQueueCapabilityList;
  }

  
  public void setEgressQueueCapabilityList(List<String> egressQueueCapabilityList) {
    this.egressQueueCapabilityList = egressQueueCapabilityList;
  }

  
  public String getRemarkMenu() {
    return remarkMenu;
  }

  
  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
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
