
package msf.fc.services.priorityroutes.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemConf", propOrder = { "priorityRoutes" })
public class SystemConf {

  @XmlElement(required = true)
  protected PriorityRoutes priorityRoutes;

  public PriorityRoutes getPriorityRoutes() {
    return priorityRoutes;
  }

  public void setPriorityRoutes(PriorityRoutes value) {
    this.priorityRoutes = value;
  }

}
