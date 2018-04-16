
package msf.mfcfc.traffic.traffics.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class IfTrafficValueNodeEntity {

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

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

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
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
