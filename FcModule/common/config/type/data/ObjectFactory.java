
package msf.fc.common.config.type.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {


  public ObjectFactory() {
  }

  public DataConf createDataConf() {
    return new DataConf();
  }

  public JAXBElement<DataConf> createDataConf(DataConf value) {
    return new JAXBElement<DataConf>(_DataConf_QNAME, DataConf.class, null, value);
  }

  public Rr createRr() {
    return new Rr();
  }

  public Rrs createRrs() {
    return new Rrs();
  }

  public SwClustersData createSwClustersData() {
    return new SwClustersData();
  }

  public SwClusterData createSwClusterData() {
    return new SwClusterData();
  }

  public Slice createSlice() {
    return new Slice();
  }

  public SwCluster createSwCluster() {
    return new SwCluster();
  }

}
