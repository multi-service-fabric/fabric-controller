
package msf.mfcfc.node.equipments.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentQosEntity {

  @SerializedName("remark")
  private Boolean remark;

  @SerializedName("remark_capability")
  private List<String> remarkCapabilityList;

  @SerializedName("remark_default")
  private String remarkDefault;

  @SerializedName("shaping")
  private Boolean shaping;

  @SerializedName("egress_queue_capability")
  private List<String> egressQueueCapabilityList;

  @SerializedName("egress_queue_default")
  private String egressQueueDefault;

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

  public String getRemarkDefault() {
    return remarkDefault;
  }

  public void setRemarkDefault(String remarkDefault) {
    this.remarkDefault = remarkDefault;
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

  public String getEgressQueueDefault() {
    return egressQueueDefault;
  }

  public void setEgressQueueDefault(String egressQueueDefault) {
    this.egressQueueDefault = egressQueueDefault;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
