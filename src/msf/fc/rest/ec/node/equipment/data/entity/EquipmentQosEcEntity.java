package msf.fc.rest.ec.node.equipment.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class EquipmentQosEcEntity {

  
  @SerializedName("shaping")
  private EquipmentShapingEcEntity shaping;

  
  @SerializedName("remark")
  private EquipmentRemarkEcEntity remark;

  
  @SerializedName("egress")
  private EquipmentEgressEcEntity egress;

  
  public EquipmentShapingEcEntity getShaping() {
    return shaping;
  }

  
  public void setShaping(EquipmentShapingEcEntity shaping) {
    this.shaping = shaping;
  }

  
  public EquipmentRemarkEcEntity getRemark() {
    return remark;
  }

  
  public void setRemark(EquipmentRemarkEcEntity remark) {
    this.remark = remark;
  }

  
  public EquipmentEgressEcEntity getEgress() {
    return egress;
  }

  
  public void setEgress(EquipmentEgressEcEntity egress) {
    this.egress = egress;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
