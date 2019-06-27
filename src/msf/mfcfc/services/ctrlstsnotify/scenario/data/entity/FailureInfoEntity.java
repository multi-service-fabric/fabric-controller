
package msf.mfcfc.services.ctrlstsnotify.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailureInfoEntity {

  @SerializedName("cpu")
  private FailureInfoCpuEntity cpu;

  @SerializedName("memory")
  private FailureInfoMemoryEntity memory;

  @SerializedName("disk")
  private FailureInfoDiskEntity disk;

  public FailureInfoCpuEntity getCpu() {
    return cpu;
  }

  public void setCpu(FailureInfoCpuEntity cpu) {
    this.cpu = cpu;
  }

  public FailureInfoMemoryEntity getMemory() {
    return memory;
  }

  public void setMemory(FailureInfoMemoryEntity memory) {
    this.memory = memory;
  }

  public FailureInfoDiskEntity getDisk() {
    return disk;
  }

  public void setDisk(FailureInfoDiskEntity disk) {
    this.disk = disk;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
