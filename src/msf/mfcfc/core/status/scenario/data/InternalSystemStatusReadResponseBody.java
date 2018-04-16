
package msf.mfcfc.core.status.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class InternalSystemStatusReadResponseBody extends AbstractResponseBody {

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
    return ToStringBuilder.reflectionToString(this);
  }

}
