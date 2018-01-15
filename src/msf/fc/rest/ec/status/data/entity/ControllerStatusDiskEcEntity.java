package msf.fc.rest.ec.status.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusDiskEcEntity {

  
  @SerializedName("devices")
  private List<ControllerStatusDeviceEcEntity> deviceList;

  
  public List<ControllerStatusDeviceEcEntity> getDeviceList() {
    return deviceList;
  }

  
  public void setDeviceList(List<ControllerStatusDeviceEcEntity> deviceList) {
    this.deviceList = deviceList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
