
package msf.mfcfc.traffic.traffics.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2CpTrafficValueCpEntity {

  @SerializedName("receive_rate")
  private Double receiveRate;

  @SerializedName("send_rate")
  private Double sendRate;

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
