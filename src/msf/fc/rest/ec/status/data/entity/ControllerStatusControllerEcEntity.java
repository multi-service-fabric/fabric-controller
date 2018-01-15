
package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusControllerEcEntity {

  
  @SerializedName("cpu")
  private Float cpu;

  
  @SerializedName("memory")
  private Integer memory;

  
  @SerializedName("receive_rest_request")
  private Integer receiveRestRequest;

  
  @SerializedName("send_rest_request")
  private Integer sendRestRequest;

  
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

  
  public Integer getReceiveRestRequest() {
    return receiveRestRequest;
  }

  
  public void setReceiveRestRequest(Integer receiveRestRequest) {
    this.receiveRestRequest = receiveRestRequest;
  }

  
  public Integer getSendRestRequest() {
    return sendRestRequest;
  }

  
  public void setSendRestRequest(Integer sendRestRequest) {
    this.sendRestRequest = sendRestRequest;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
