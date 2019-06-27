
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControllerProcess", propOrder = { "cpuUseRateThreshold", "memoryUsageThreshold", "disk" })
public class ControllerProcess {

  @XmlSchemaType(name = "float")
  protected float cpuUseRateThreshold;
  @XmlSchemaType(name = "integer")
  protected int memoryUsageThreshold;
  @XmlElement(required = true)
  protected DiskProcess disk;

  public float getCpuUseRateThreshold() {
    return cpuUseRateThreshold;
  }

  public void setCpuUseRateThreshold(float value) {
    this.cpuUseRateThreshold = value;
  }

  public int getMemoryUsageThreshold() {
    return memoryUsageThreshold;
  }

  public void setMemoryUsageThreshold(int value) {
    this.memoryUsageThreshold = value;
  }

  public DiskProcess getDisk() {
    return disk;
  }

  public void setDisk(DiskProcess value) {
    this.disk = value;
  }

}
