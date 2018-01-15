package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusOsEcEntity {

  
  @SerializedName("cpu")
  private ControllerStatusCpuEcEntity cpu;

  
  @SerializedName("memory")
  private ControllerStatusMemoryEcEntity memory;

  
  @SerializedName("disk")
  private ControllerStatusDiskEcEntity disk;

  
  @SerializedName("traffic")
  private ControllerStatusTrafficEcEntity traffic;

  
  public ControllerStatusCpuEcEntity getCpu() {
    return cpu;
  }

  
  public void setCpu(ControllerStatusCpuEcEntity cpu) {
    this.cpu = cpu;
  }

  
  public ControllerStatusMemoryEcEntity getMemory() {
    return memory;
  }

  
  public void setMemory(ControllerStatusMemoryEcEntity memory) {
    this.memory = memory;
  }

  
  public ControllerStatusDiskEcEntity getDisk() {
    return disk;
  }

  
  public void setDisk(ControllerStatusDiskEcEntity disk) {
    this.disk = disk;
  }

  
  public ControllerStatusTrafficEcEntity getTraffic() {
    return traffic;
  }

  
  public void setTraffic(ControllerStatusTrafficEcEntity traffic) {
    this.traffic = traffic;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
