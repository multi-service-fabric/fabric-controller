
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private static final QName _SystemConf_QNAME = new QName(
      "http://fc.msf/services/ctrlstsnotify/common/config/type/system", "systemConf");

  public ObjectFactory() {
  }

  public SystemConf createSystemConf() {
    return new SystemConf();
  }

  @XmlElementDecl(namespace = "http://fc.msf/services/ctrlstsnotify/common/config/type/system", name = "systemConf")
  public JAXBElement<SystemConf> createSystemConf(SystemConf value) {
    return new JAXBElement<SystemConf>(_SystemConf_QNAME, SystemConf.class, null, value);
  }

  public LogNotification createLogNotification() {
    return new LogNotification();
  }

  public Os createOs() {
    return new Os();
  }

  public DiskOs createDiskOs() {
    return new DiskOs();
  }

  public Device createDevice() {
    return new Device();
  }

  public NoticeDestInfoLog createNoticeDestInfoLog() {
    return new NoticeDestInfoLog();
  }

  public LogLevel createLogLevel() {
    return new LogLevel();
  }

  public ControllerProcess createControllerProcess() {
    return new ControllerProcess();
  }

  public DiskProcess createDiskProcess() {
    return new DiskProcess();
  }

  public Monitored createMonitored() {
    return new Monitored();
  }

  public NoticeDestInfoCtlFailure createNoticeDestInfoCtlFailure() {
    return new NoticeDestInfoCtlFailure();
  }

  public ControllerFailureNotification createControllerFailureNotification() {
    return new ControllerFailureNotification();
  }

}
