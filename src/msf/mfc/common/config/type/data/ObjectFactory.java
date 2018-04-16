
package msf.mfc.common.config.type.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private static final QName _DataConf_QNAME = new QName("http://mfc.msf/common/config/type/data", "dataConf");

  public ObjectFactory() {
  }

  public DataConf createDataConf() {
    return new DataConf();
  }

  @XmlElementDecl(namespace = "http://mfc.msf/common/config/type/data", name = "dataConf")
  public JAXBElement<DataConf> createDataConf(DataConf value) {
    return new JAXBElement<DataConf>(_DataConf_QNAME, DataConf.class, null, value);
  }

  public Rr createRr() {
    return new Rr();
  }

  public SwClusterData createSwClusterData() {
    return new SwClusterData();
  }

  public Rrs createRrs() {
    return new Rrs();
  }

  public SwClustersData createSwClustersData() {
    return new SwClustersData();
  }

  public SwCluster createSwCluster() {
    return new SwCluster();
  }

}
