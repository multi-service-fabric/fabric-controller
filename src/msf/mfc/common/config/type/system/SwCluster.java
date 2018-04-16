
package msf.mfc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwCluster", propOrder = {})
public class SwCluster {

  protected int swClusterId;
  @XmlElement(required = true)
  protected String fcControlAddress;
  @XmlSchemaType(name = "integer")
  protected int fcControlPort;

  public int getSwClusterId() {
    return swClusterId;
  }

  public void setSwClusterId(int value) {
    this.swClusterId = value;
  }

  public String getFcControlAddress() {
    return fcControlAddress;
  }

  public void setFcControlAddress(String value) {
    this.fcControlAddress = value;
  }

  public int getFcControlPort() {
    return fcControlPort;
  }

  public void setFcControlPort(int value) {
    this.fcControlPort = value;
  }

}
