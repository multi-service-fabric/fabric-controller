package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusEcStatusEcEntity {

  
  @SerializedName("status")
  private String status;

  
  @SerializedName("busy")
  private String busy;

  
  public String getStatus() {
    return status;
  }

  
  public void setStatus(String status) {
    this.status = status;
  }

  
  public String getBusy() {
    return busy;
  }

  
  public void setBusy(String busy) {
    this.busy = busy;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
