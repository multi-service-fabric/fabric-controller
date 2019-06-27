
package msf.mfcfc.services.ctrlstsnotify.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.services.ctrlstsnotify.common.constant.ControllerErrorType;
import msf.mfcfc.services.ctrlstsnotify.common.constant.SystemType;

public class ControllerErrorNotificationEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("controller_type")
  private String controllerType;

  @SerializedName("system_type")
  private String systemType;

  @SerializedName("failure_info")
  private FailureInfoEntity failureInfo;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getControllerType() {
    return controllerType;
  }

  public void setControllerType(String controllerType) {
    this.controllerType = controllerType;
  }

  public String getSystemType() {
    return systemType;
  }

  public void setSystemType(String systemType) {
    this.systemType = systemType;
  }

  public FailureInfoEntity getFailureInfo() {
    return failureInfo;
  }

  public void setFailureInfo(FailureInfoEntity failureInfo) {
    this.failureInfo = failureInfo;
  }

  public ControllerErrorType getControllerTypeEnum() {
    return ControllerErrorType.getEnumFromMessage(controllerType);
  }

  public void setControllerTypeEnum(ControllerErrorType controllerType) {
    this.controllerType = controllerType.getMessage();
  }

  public SystemType getSystemTypeEnum() {
    return SystemType.getEnumFromMessage(systemType);
  }

  public void setSystemTypeEnum(SystemType systemType) {
    this.systemType = systemType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
