
package msf.fc.common.config.type.develop;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private static final QName _DevelopConf_QNAME = new QName("http://fc.msf/common/config/type/develop", "developConf");

  public ObjectFactory() {
  }

  public DevelopConf createDevelopConf() {
    return new DevelopConf();
  }

  @XmlElementDecl(namespace = "http://fc.msf/common/config/type/develop", name = "developConf")
  public JAXBElement<DevelopConf> createDevelopConf(DevelopConf value) {
    return new JAXBElement<DevelopConf>(_DevelopConf_QNAME, DevelopConf.class, null, value);
  }

  public AsyncOperation createAsyncOperation() {
    return new AsyncOperation();
  }

  public Notice createNotice() {
    return new Notice();
  }

  public System createSystem() {
    return new System();
  }

  public Lock createLock() {
    return new Lock();
  }
}
