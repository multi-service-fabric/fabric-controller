
package msf.fc.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Status", propOrder = { "noticeDestInfo", "noticeRetryNum", "noticeTimeout", "recvRestRequestUnitTime",
    "sendRestRequestUnitTime" })
public class Status {

  protected List<NoticeDestInfoStatus> noticeDestInfo;
  @XmlSchemaType(name = "integer")
  protected int noticeRetryNum;
  @XmlSchemaType(name = "integer")
  protected int noticeTimeout;
  @XmlSchemaType(name = "integer")
  protected int recvRestRequestUnitTime;
  @XmlSchemaType(name = "integer")
  protected int sendRestRequestUnitTime;

  public List<NoticeDestInfoStatus> getNoticeDestInfo() {
    if (noticeDestInfo == null) {
      noticeDestInfo = new ArrayList<NoticeDestInfoStatus>();
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

  public int getRecvRestRequestUnitTime() {
    return recvRestRequestUnitTime;
  }

  public void setRecvRestRequestUnitTime(int value) {
    this.recvRestRequestUnitTime = value;
  }

  public int getSendRestRequestUnitTime() {
    return sendRestRequestUnitTime;
  }

  public void setSendRestRequestUnitTime(int value) {
    this.sendRestRequestUnitTime = value;
  }

}
