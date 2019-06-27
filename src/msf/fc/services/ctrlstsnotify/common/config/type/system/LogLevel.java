
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogLevel", propOrder = { "info", "warning", "error" })
public class LogLevel {

  protected boolean info;
  protected boolean warning;
  protected boolean error;

  public boolean isInfo() {
    return info;
  }

  public void setInfo(boolean value) {
    this.info = value;
  }

  public boolean isWarning() {
    return warning;
  }

  public void setWarning(boolean value) {
    this.warning = value;
  }

  public boolean isError() {
    return error;
  }

  public void setError(boolean value) {
    this.error = value;
  }

}
