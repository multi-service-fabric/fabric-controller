
package msf.fc.common.config.type.system;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {


  public ObjectFactory() {
  }

  public SystemConf createSystemConf() {
    return new SystemConf();
  }

  public JAXBElement<SystemConf> createSystemConf(SystemConf value) {
    return new JAXBElement<SystemConf>(_SystemConf_QNAME, SystemConf.class, null, value);
  }

  public Server createServer() {
    return new Server();
  }

  public SwClusterData createSwClusterData() {
    return new SwClusterData();
  }

  public Json createJson() {
    return new Json();
  }

  public Traffic createTraffic() {
    return new Traffic();
  }

  public Rest createRest() {
    return new Rest();
  }

  public SwClustersData createSwClustersData() {
    return new SwClustersData();
  }

  public Slice createSlice() {
    return new Slice();
  }

  public SwCluster createSwCluster() {
    return new SwCluster();
  }

  public Client createClient() {
    return new Client();
  }

}
