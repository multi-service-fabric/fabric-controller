package msf.mfcfc.node.interfaces.physicalifs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class PhysicalIfQosEntity {
  
  @SerializedName("remark")
  private Boolean remark;

  
  @SerializedName("remark_capability")
  private List<String> remarkCapabilityList;

  
  @SerializedName("shaping")
  private Boolean shaping;

  
  @SerializedName("egress_queue_capability")
  private List<String> egressQueueCapabilityList;

  
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

  
  public List<String> getEgressQueueCapabilityList() {
    return egressQueueCapabilityList;
  }

  
  public void setEgressQueueCapabilityList(List<String> egressQueueCapabilityList) {
    this.egressQueueCapabilityList = egressQueueCapabilityList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
