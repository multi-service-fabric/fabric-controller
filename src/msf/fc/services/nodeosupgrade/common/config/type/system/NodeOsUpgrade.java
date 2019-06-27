
package msf.fc.services.nodeosupgrade.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeOsUpgrade", propOrder = { "internalLinkDetourIgpCost" })
public class NodeOsUpgrade {

  @XmlSchemaType(name = "integer")
  protected int internalLinkDetourIgpCost;

  public int getInternalLinkDetourIgpCost() {
    return internalLinkDetourIgpCost;
  }

  public void setInternalLinkDetourIgpCost(int value) {
    this.internalLinkDetourIgpCost = value;
  }

}
