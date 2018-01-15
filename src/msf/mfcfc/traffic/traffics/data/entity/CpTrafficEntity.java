package msf.mfcfc.traffic.traffics.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class CpTrafficEntity {

  
  @SerializedName("slice_id")
  private String sliceId;

  
  @SerializedName("cp_id")
  private String cpId;

  
  @SerializedName("traffic_value")
  private CpTrafficValueCpEntity trafficValue;

  
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

  
  public CpTrafficValueCpEntity getTrafficValue() {
    return trafficValue;
  }

  
  public void setTrafficValue(CpTrafficValueCpEntity trafficValue) {
    this.trafficValue = trafficValue;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
