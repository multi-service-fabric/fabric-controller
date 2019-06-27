
package msf.fc.services.silentfailure.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoticeDestInfo", propOrder = { "noticeAddress", "noticePort" })
public class NoticeDestInfo {

  @XmlElement(required = true)
  protected String noticeAddress;
  @XmlSchemaType(name = "integer")
  protected int noticePort;

  public String getNoticeAddress() {
    return noticeAddress;
  }

  public void setNoticeAddress(String value) {
    this.noticeAddress = value;
  }

  public int getNoticePort() {
    return noticePort;
  }

  public void setNoticePort(int value) {
    this.noticePort = value;
  }

}
