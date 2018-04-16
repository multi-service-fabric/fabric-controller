
package msf.fc.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Qos", propOrder = { "remarkMenu" })
public class Qos {

  @XmlElement(name = "remark_menu")
  protected List<String> remarkMenu;

  public List<String> getRemarkMenu() {
    if (remarkMenu == null) {
      remarkMenu = new ArrayList<String>();
    }
    return this.remarkMenu;
  }

}
