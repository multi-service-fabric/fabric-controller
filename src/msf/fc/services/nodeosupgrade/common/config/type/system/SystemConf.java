
package msf.fc.services.nodeosupgrade.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemConf", propOrder = { "nodeOsUpgrade" })
public class SystemConf {

  @XmlElement(required = true)
  protected NodeOsUpgrade nodeOsUpgrade;

  public NodeOsUpgrade getNodeOsUpgrade() {
    return nodeOsUpgrade;
  }

  public void setNodeOsUpgrade(NodeOsUpgrade value) {
    this.nodeOsUpgrade = value;
  }

}
