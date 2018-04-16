
package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SystemStatusOsEntity {

  @SerializedName("cpu")
  private SystemStatusCpuEntity cpu;

  @SerializedName("memory")
  private SystemStatusMemoryEntity memory;

  @SerializedName("disk")
  private SystemStatusDiskEntity disk;

  @SerializedName("traffic")
  private SystemStatusTrafficEntity traffic;

  public SystemStatusCpuEntity getCpu() {
    return cpu;
  }

  public void setCpu(SystemStatusCpuEntity cpu) {
    this.cpu = cpu;
  }

  public SystemStatusMemoryEntity getMemory() {
    return memory;
  }

  public void setMemory(SystemStatusMemoryEntity memory) {
    this.memory = memory;
  }

  public SystemStatusDiskEntity getDisk() {
    return disk;
  }

  public void setDisk(SystemStatusDiskEntity disk) {
    this.disk = disk;
  }

  public SystemStatusTrafficEntity getTraffic() {
    return traffic;
  }

  public void setTraffic(SystemStatusTrafficEntity traffic) {
    this.traffic = traffic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
