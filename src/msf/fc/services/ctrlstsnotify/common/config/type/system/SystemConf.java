
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemConf", propOrder = {})
public class SystemConf {

  @XmlElement(required = true)
  protected LogNotification logNotification;
  @XmlElement(required = true)
  protected ControllerFailureNotification controllerFailureNotification;

  public LogNotification getLogNotification() {
    return logNotification;
  }

  public void setLogNotification(LogNotification value) {
    this.logNotification = value;
  }

  public ControllerFailureNotification getControllerFailureNotification() {
    return controllerFailureNotification;
  }

  public void setControllerFailureNotification(ControllerFailureNotification value) {
    this.controllerFailureNotification = value;
  }

}
