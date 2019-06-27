
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiskProcess", propOrder = { "usageThreshold" })
public class DiskProcess {

  @XmlSchemaType(name = "integer")
  protected int usageThreshold;

  public int getUsageThreshold() {
    return usageThreshold;
  }

  public void setUsageThreshold(int value) {
    this.usageThreshold = value;
  }

}
