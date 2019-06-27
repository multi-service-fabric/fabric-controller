
package msf.fc.services.silentfailure.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SilentFailure", propOrder = { "noticeDestInfo", "noticeRetryNum", "noticeTimeout", "pingMonitorCycle",
    "ospfNeighborMonitorCycle", "enableExecutingIfBlockade" })
public class SilentFailure {

  protected List<NoticeDestInfo> noticeDestInfo;
  @XmlSchemaType(name = "integer")
  protected int noticeRetryNum;
  @XmlSchemaType(name = "integer")
  protected int noticeTimeout;
  @XmlSchemaType(name = "integer")
  protected int pingMonitorCycle;
  @XmlSchemaType(name = "integer")
  protected int ospfNeighborMonitorCycle;
  protected boolean enableExecutingIfBlockade;

  public List<NoticeDestInfo> getNoticeDestInfo() {
    if (noticeDestInfo == null) {
      noticeDestInfo = new ArrayList<NoticeDestInfo>();
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

  public int getPingMonitorCycle() {
    return pingMonitorCycle;
  }

  public void setPingMonitorCycle(int value) {
    this.pingMonitorCycle = value;
  }

  public int getOspfNeighborMonitorCycle() {
    return ospfNeighborMonitorCycle;
  }

  public void setOspfNeighborMonitorCycle(int value) {
    this.ospfNeighborMonitorCycle = value;
  }

  public boolean isEnableExecutingIfBlockade() {
    return enableExecutingIfBlockade;
  }

  public void setEnableExecutingIfBlockade(boolean value) {
    this.enableExecutingIfBlockade = value;
  }

}
