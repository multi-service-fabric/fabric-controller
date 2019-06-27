
package msf.fc.services.priorityroutes.common.config.type.system;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private static final QName _SystemConf_QNAME = new QName(
      "http://fc.msf/services/priorityroutes/common/config/type/system", "systemConf");

  public ObjectFactory() {
  }

  public SystemConf createSystemConf() {
    return new SystemConf();
  }

  @XmlElementDecl(namespace = "http://fc.msf/services/priorityroutes/common/config/type/system", name = "systemConf")
  public JAXBElement<SystemConf> createSystemConf(SystemConf value) {
    return new JAXBElement<SystemConf>(_SystemConf_QNAME, SystemConf.class, null, value);
  }

  public PriorityRoutes createPriorityRoutes() {
    return new PriorityRoutes();
  }

}
