
package msf.mfc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoticeDestInfoFailure", propOrder = { "noticeAddress", "noticePort", "isPhysicalUnit", "isClusterUnit",
    "isSliceUnit" })
public class NoticeDestInfoFailure {

  @XmlElement(required = true)
  protected String noticeAddress;
  @XmlSchemaType(name = "integer")
  protected int noticePort;
  protected boolean isPhysicalUnit;
  protected boolean isClusterUnit;
  protected boolean isSliceUnit;

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

  public boolean isIsPhysicalUnit() {
    return isPhysicalUnit;
  }

  public void setIsPhysicalUnit(boolean value) {
    this.isPhysicalUnit = value;
  }

  public boolean isIsClusterUnit() {
    return isClusterUnit;
  }

  public void setIsClusterUnit(boolean value) {
    this.isClusterUnit = value;
  }

  public boolean isIsSliceUnit() {
    return isSliceUnit;
  }

  public void setIsSliceUnit(boolean value) {
    this.isSliceUnit = value;
  }

}
