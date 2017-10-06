package msf.fc.core.status.scenario.data;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class SystemStatusReadResponseBody extends AbstractResponseBody {
  @SerializedName("service_status")
  private String serviceStatus;

  @SerializedName("blockade_status")
  private String blockadeStatus;

  public String getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(String serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public String getBlockadeStatus() {
    return blockadeStatus;
  }

  public void setBlockadeStatus(String blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  @Override
  public String toString() {
    return "SystemStatusReadResponseBody [serviceStatus=" + serviceStatus + ", blockadeStatus=" + blockadeStatus
        + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
  }

}
