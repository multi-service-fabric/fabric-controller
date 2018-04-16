
package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ServiceStatus;

public class SystemStatusServiceStatusEntity {

  @SerializedName("service_status")
  private String serviceStatus;

  public String getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(String serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public ServiceStatus getServiceStatusEnum() {
    return ServiceStatus.getEnumFromMessage(serviceStatus);
  }

  public void setServiceStatusEnum(ServiceStatus serviceStatusType) {
    this.serviceStatus = serviceStatusType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
