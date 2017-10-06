package msf.fc.rest.ec.traffic.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.CpCreateIfType;

public class L2CpEcEntity {
  @SerializedName("if_type")
  private String ifType;

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("receive_rate")
  private Double receiveRate;

  @SerializedName("send_rate")
  private Double sendRate;

  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  public CpCreateIfType getIfTypeEnum() {
    return CpCreateIfType.getEnumFromMessage(ifType);
  }

  public void setIfTypeEnum(CpCreateIfType ifType) {
    this.ifType = ifType.getMessage();
  }

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public Double getReceiveRate() {
    return receiveRate;
  }

  public void setReceiveRate(Double receiveRate) {
    this.receiveRate = receiveRate;
  }

  public Double getSendRate() {
    return sendRate;
  }

  public void setSendRate(Double sendRate) {
    this.sendRate = sendRate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
