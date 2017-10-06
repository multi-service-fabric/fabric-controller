
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Traffic", propOrder = {})
public class Traffic {

  @XmlSchemaType(name = "integer")
  protected int interval;
  @XmlSchemaType(name = "integer")
  protected int dataRetentionPeriod;
  @XmlElement(required = true)
  protected String tmToolPath;
  @XmlElement(required = true)
  protected String tmInputFilePath;
  @XmlElement(required = true)
  protected String tmOutputFilePath;

  public int getInterval() {
    return interval;
  }

  public void setInterval(int value) {
    this.interval = value;
  }

  public int getDataRetentionPeriod() {
    return dataRetentionPeriod;
  }

  public void setDataRetentionPeriod(int value) {
    this.dataRetentionPeriod = value;
  }

  public String getTmToolPath() {
    return tmToolPath;
  }

  public void setTmToolPath(String value) {
    this.tmToolPath = value;
  }

  public String getTmInputFilePath() {
    return tmInputFilePath;
  }

  public void setTmInputFilePath(String value) {
    this.tmInputFilePath = value;
  }

  public String getTmOutputFilePath() {
    return tmOutputFilePath;
  }

  public void setTmOutputFilePath(String value) {
    this.tmOutputFilePath = value;
  }

}
