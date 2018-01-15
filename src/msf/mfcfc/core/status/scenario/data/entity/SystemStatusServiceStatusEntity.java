package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SystemStatusServiceStatusEntity {

  
  @SerializedName("service_status")
  private String serviceStatus;

  
  public String getServiceStatus() {
    return serviceStatus;
  }

  
  public void setServiceStatus(String serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
