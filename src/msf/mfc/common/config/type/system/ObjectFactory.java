
package msf.mfc.common.config.type.system;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private static final QName _SystemConf_QNAME = new QName("http://mfc.msf/common/config/type/system", "systemConf");

  public ObjectFactory() {
  }

  public SystemConf createSystemConf() {
    return new SystemConf();
  }

  @XmlElementDecl(namespace = "http://mfc.msf/common/config/type/system", name = "systemConf")
  public JAXBElement<SystemConf> createSystemConf(SystemConf value) {
    return new JAXBElement<SystemConf>(_SystemConf_QNAME, SystemConf.class, null, value);
  }

  public Status createStatus() {
    return new Status();
  }

  public Server createServer() {
    return new Server();
  }

  public SwClusterData createSwClusterData() {
    return new SwClusterData();
  }

  public Traffic createTraffic() {
    return new Traffic();
  }

  public NoticeDestInfoStatus createNoticeDestInfoStatus() {
    return new NoticeDestInfoStatus();
  }

  public Json createJson() {
    return new Json();
  }

  public Controller createController() {
    return new Controller();
  }

  public NoticeDestInfoTraffic createNoticeDestInfoTraffic() {
    return new NoticeDestInfoTraffic();
  }

  public NoticeDestInfoFailure createNoticeDestInfoFailure() {
    return new NoticeDestInfoFailure();
  }

  public Rest createRest() {
    return new Rest();
  }

  public Rrs createRrs() {
    return new Rrs();
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

  public Failure createFailure() {
    return new Failure();
  }

}
