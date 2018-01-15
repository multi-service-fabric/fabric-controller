package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SystemStatusDeviceEntity {

  
  @SerializedName("file_system")
  private String fileSystem;

  
  @SerializedName("mounted_on")
  private String mountedOn;

  
  @SerializedName("size")
  private Integer size;

  
  @SerializedName("used")
  private Integer used;

  
  @SerializedName("avail")
  private Integer avail;

  
  public String getFileSystem() {
    return fileSystem;
  }

  
  public void setFileSystem(String fileSystem) {
    this.fileSystem = fileSystem;
  }

  
  public String getMountedOn() {
    return mountedOn;
  }

  
  public void setMountedOn(String mountedOn) {
    this.mountedOn = mountedOn;
  }

  
  public Integer getSize() {
    return size;
  }

  
  public void setSize(Integer size) {
    this.size = size;
  }

  
  public Integer getUsed() {
    return used;
  }

  
  public void setUsed(Integer used) {
    this.used = used;
  }

  
  public Integer getAvail() {
    return avail;
  }

  
  public void setAvail(Integer avail) {
    this.avail = avail;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
