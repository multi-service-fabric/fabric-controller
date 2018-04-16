
package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SystemStatusControllerServiceStatusEntity {

  @SerializedName("controller_type")
  private String controllerType;

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("status")
  private SystemStatusServiceStatusEntity status;

  public String getControllerType() {
    return controllerType;
  }

  public void setControllerType(String controllerType) {
    this.controllerType = controllerType;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public SystemStatusServiceStatusEntity getStatus() {
    return status;
  }

  public void setStatus(SystemStatusServiceStatusEntity status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
