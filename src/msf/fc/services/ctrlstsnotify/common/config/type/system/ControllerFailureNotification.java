
package msf.fc.services.ctrlstsnotify.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControllerFailureNotification", propOrder = { "noticeDestInfo", "noticeRetryNum", "noticeTimeout",
    "monitoringPeriod" })
public class ControllerFailureNotification {

  protected List<NoticeDestInfoCtlFailure> noticeDestInfo;
  @XmlSchemaType(name = "integer")
  protected int noticeRetryNum;
  @XmlSchemaType(name = "integer")
  protected int noticeTimeout;
  @XmlSchemaType(name = "integer")
  protected int monitoringPeriod;

  public List<NoticeDestInfoCtlFailure> getNoticeDestInfo() {
    if (noticeDestInfo == null) {
      noticeDestInfo = new ArrayList<NoticeDestInfoCtlFailure>();
    }
    return this.noticeDestInfo;
  }

  public int getNoticeRetryNum() {
    return noticeRetryNum;
  }

  public void setNoticeRetryNum(int value) {
    this.noticeRetryNum = value;
  }

  public int getNoticeTimeout() {
    return noticeTimeout;
  }

  public void setNoticeTimeout(int value) {
    this.noticeTimeout = value;
  }

  public int getMonitoringPeriod() {
    return monitoringPeriod;
  }

  public void setMonitoringPeriod(int value) {
    this.monitoringPeriod = value;
  }

}
