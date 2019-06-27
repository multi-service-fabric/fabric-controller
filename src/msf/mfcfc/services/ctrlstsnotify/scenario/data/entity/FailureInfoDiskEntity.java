
package msf.mfcfc.services.ctrlstsnotify.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailureInfoDiskEntity {

  @SerializedName("devices")
  private List<DeviceListEntity> deviceList;

  public List<DeviceListEntity> getDeviceList() {
    return deviceList;
  }

  public void setDeviceList(List<DeviceListEntity> deviceList) {
    this.deviceList = deviceList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
