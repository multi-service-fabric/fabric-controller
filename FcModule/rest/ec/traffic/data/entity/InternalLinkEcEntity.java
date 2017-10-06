package msf.fc.rest.ec.traffic.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalLinkEcEntity {
  @SerializedName("internal_link_if_id")
  private String internalLinkIfId;

  @SerializedName("receive_rate")
  private Double receiveRate;

  @SerializedName("send_rate")
  private Double sendRate;

  public String getInternalLinkIfId() {
    return internalLinkIfId;
  }

  public void setInternalLinkIfId(String internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
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
