package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SystemStatusControllerEntity {

  
  @SerializedName("cpu")
  private Float cpu;

  
  @SerializedName("memory")
  private Integer memory;

  
  @SerializedName("receive_request")
  private Integer receiveRequest;

  
  @SerializedName("send_request")
  private Integer sendRequest;

  
  public Float getCpu() {
    return cpu;
  }

  
  public void setCpu(Float cpu) {
    this.cpu = cpu;
  }

  
  public Integer getMemory() {
    return memory;
  }

  
  public void setMemory(Integer memory) {
    this.memory = memory;
  }

  
  public Integer getReceiveRequest() {
    return receiveRequest;
  }

  
  public void setReceiveRequest(Integer receiveRequest) {
    this.receiveRequest = receiveRequest;
  }

  
  public Integer getSendRequest() {
    return sendRequest;
  }

  
  public void setSendRequest(Integer sendRequest) {
    this.sendRequest = sendRequest;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
