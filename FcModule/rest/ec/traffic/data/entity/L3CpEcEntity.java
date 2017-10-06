package msf.fc.rest.ec.traffic.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L3CpEcEntity {
  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("receive_rate")
  private Double receiveRate;

  @SerializedName("send_rate")
  private Double sendRate;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
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
