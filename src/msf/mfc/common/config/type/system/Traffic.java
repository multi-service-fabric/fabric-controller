
package msf.mfc.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Traffic", propOrder = { "noticeDestInfo", "noticeRetryNum", "noticeTimeout" })
public class Traffic {

  protected List<NoticeDestInfoTraffic> noticeDestInfo;
  @XmlSchemaType(name = "integer")
  protected int noticeRetryNum;
  @XmlSchemaType(name = "integer")
  protected int noticeTimeout;

  public List<NoticeDestInfoTraffic> getNoticeDestInfo() {
    if (noticeDestInfo == null) {
      noticeDestInfo = new ArrayList<NoticeDestInfoTraffic>();
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

}
