
package msf.fc.services.priorityroutes.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PriorityRoutes", propOrder = { "internalLinkPriorityIgpCost" })
public class PriorityRoutes {

  @XmlSchemaType(name = "integer")
  protected int internalLinkPriorityIgpCost;

  public int getInternalLinkPriorityIgpCost() {
    return internalLinkPriorityIgpCost;
  }

  public void setInternalLinkPriorityIgpCost(int value) {
    this.internalLinkPriorityIgpCost = value;
  }

}
