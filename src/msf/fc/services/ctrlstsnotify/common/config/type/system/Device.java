
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Device", propOrder = { "fileSystem", "usageThreshold" })
public class Device {

  @XmlElement(required = true)
  protected String fileSystem;
  @XmlSchemaType(name = "integer")
  protected int usageThreshold;

  public String getFileSystem() {
    return fileSystem;
  }

  public void setFileSystem(String value) {
    this.fileSystem = value;
  }

  public int getUsageThreshold() {
    return usageThreshold;
  }

  public void setUsageThreshold(int value) {
    this.usageThreshold = value;
  }

}
