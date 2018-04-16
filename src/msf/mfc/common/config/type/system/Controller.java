
package msf.mfc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Controller", propOrder = { "managementIpAddress" })
public class Controller {

  @XmlElement(required = true)
  protected String managementIpAddress;

  public String getManagementIpAddress() {
    return managementIpAddress;
  }

  public void setManagementIpAddress(String value) {
    this.managementIpAddress = value;
  }

}
