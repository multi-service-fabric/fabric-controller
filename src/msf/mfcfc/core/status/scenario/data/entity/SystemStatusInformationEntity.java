package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SystemStatusInformationEntity {

  
  @SerializedName("controller_type")
  private String controllerType;

  
  @SerializedName("cluster_id")
  private String clusterId;

  
  @SerializedName("host_name")
  private String hostName;

  
  @SerializedName("management_ip_address")
  private String managementIpAddress;

  
  @SerializedName("os")
  private SystemStatusOsEntity os;

  
  @SerializedName("controller")
  private SystemStatusControllerEntity controller;

  
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

  
  public String getHostName() {
    return hostName;
  }

  
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  
  public String getManagementIpAddress() {
    return managementIpAddress;
  }

  
  public void setManagementIpAddress(String managementIpAddress) {
    this.managementIpAddress = managementIpAddress;
  }

  
  public SystemStatusOsEntity getOs() {
    return os;
  }

  
  public void setOs(SystemStatusOsEntity os) {
    this.os = os;
  }

  
  public SystemStatusControllerEntity getController() {
    return controller;
  }

  
  public void setController(SystemStatusControllerEntity controller) {
    this.controller = controller;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
