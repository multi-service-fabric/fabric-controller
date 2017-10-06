
package msf.fc.common.config.type.develop;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {


  public ObjectFactory() {
  }

  public DevelopConf createDevelopConf() {
    return new DevelopConf();
  }

  public JAXBElement<DevelopConf> createDevelopConf(DevelopConf value) {
    return new JAXBElement<DevelopConf>(_DevelopConf_QNAME, DevelopConf.class, null, value);
  }

  public AsyncOperation createAsyncOperation() {
    return new AsyncOperation();
  }

  public System createSystem() {
    return new System();
  }

  public Lock createLock() {
    return new Lock();
  }

}
