package msf.mfcfc.core.status.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SystemStatusDiskEntity {

  
  @SerializedName("devices")
  private List<SystemStatusDeviceEntity> deviceList;

  
  public List<SystemStatusDeviceEntity> getDeviceList() {
    return deviceList;
  }

  
  public void setDeviceList(List<SystemStatusDeviceEntity> deviceList) {
    this.deviceList = deviceList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
