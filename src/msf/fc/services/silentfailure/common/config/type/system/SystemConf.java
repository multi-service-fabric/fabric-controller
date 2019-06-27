
package msf.fc.services.silentfailure.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemConf", propOrder = { "silentFailure" })
public class SystemConf {

  @XmlElement(required = true)
  protected SilentFailure silentFailure;

  public SilentFailure getSilentFailure() {
    return silentFailure;
  }

  public void setSilentFailure(SilentFailure value) {
    this.silentFailure = value;
  }

}
