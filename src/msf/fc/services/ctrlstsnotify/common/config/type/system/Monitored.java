
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Monitored", propOrder = { "os", "controllerProcess" })
public class Monitored {

  @XmlElement(required = true)
  protected Os os;
  @XmlElement(required = true)
  protected ControllerProcess controllerProcess;

  public Os getOs() {
    return os;
  }

  public void setOs(Os value) {
    this.os = value;
  }

  public ControllerProcess getControllerProcess() {
    return controllerProcess;
  }

  public void setControllerProcess(ControllerProcess value) {
    this.controllerProcess = value;
  }

}
