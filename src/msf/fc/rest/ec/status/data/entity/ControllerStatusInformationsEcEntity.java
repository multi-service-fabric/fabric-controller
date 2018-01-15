package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusInformationsEcEntity {

  
  @SerializedName("controller_type")
  private String controllerType;

  
  @SerializedName("host_name")
  private String hostName;

  
  @SerializedName("management_ip_address")
  private String managementIpAddress;

  
  @SerializedName("os")
  private ControllerStatusOsEcEntity os;

  
  @SerializedName("controller")
  private ControllerStatusControllerEcEntity controller;

  
  public String getControllerType() {
    return controllerType;
  }

  
  public void setControllerType(String controllerType) {
    this.controllerType = controllerType;
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

  
  public ControllerStatusOsEcEntity getOs() {
    return os;
  }

  
  public void setOs(ControllerStatusOsEcEntity os) {
    this.os = os;
  }

  
  public ControllerStatusControllerEcEntity getController() {
    return controller;
  }

  
  public void setController(ControllerStatusControllerEcEntity controller) {
    this.controller = controller;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
