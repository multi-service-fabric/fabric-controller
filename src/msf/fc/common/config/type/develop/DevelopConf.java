
package msf.fc.common.config.type.develop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DevelopConf", propOrder = {})
public class DevelopConf {

  @XmlElement(required = true)
  protected System system;

  public System getSystem() {
    return system;
  }

  public void setSystem(System value) {
    this.system = value;
  }

}
