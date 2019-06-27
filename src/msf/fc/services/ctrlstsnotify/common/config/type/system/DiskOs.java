
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiskOs", propOrder = { "devices" })
public class DiskOs {

  protected List<Device> devices;

  public List<Device> getDevices() {
    if (devices == null) {
      devices = new ArrayList<Device>();
    }
    return this.devices;
  }

}
