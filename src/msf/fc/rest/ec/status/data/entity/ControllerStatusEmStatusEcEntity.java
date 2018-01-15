package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusEmStatusEcEntity {

  
  @SerializedName("status")
  private String status;

  
  public String getStatus() {
    return status;
  }

  
  public void setStatus(String status) {
    this.status = status;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
