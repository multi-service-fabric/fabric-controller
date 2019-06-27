
package msf.fc.services.silentfailure.common.config.type.system;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private static final QName _SystemConf_QNAME = new QName(
      "http://fc.msf/services/silentfailure/common/config/type/system", "systemConf");

  public ObjectFactory() {
  }

  public SystemConf createSystemConf() {
    return new SystemConf();
  }

  @XmlElementDecl(namespace = "http://fc.msf/services/silentfailure/common/config/type/system", name = "systemConf")
  public JAXBElement<SystemConf> createSystemConf(SystemConf value) {
    return new JAXBElement<SystemConf>(_SystemConf_QNAME, SystemConf.class, null, value);
  }

  public SilentFailure createSilentFailure() {
    return new SilentFailure();
  }

  public NoticeDestInfo createNoticeDestInfo() {
    return new NoticeDestInfo();
  }

}
