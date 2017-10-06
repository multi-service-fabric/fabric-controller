
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwCluster", propOrder = {})
public class SwCluster {

  @XmlSchemaType(name = "integer")
  protected int swClusterId;
  @XmlElement(required = true)
  protected String ecControlAddress;
  @XmlSchemaType(name = "integer")
  protected int ecControlPort;

  public int getSwClusterId() {
    return swClusterId;
  }

  public void setSwClusterId(int value) {
    this.swClusterId = value;
  }

  public String getEcControlAddress() {
    return ecControlAddress;
  }

  public void setEcControlAddress(String value) {
    this.ecControlAddress = value;
  }

  public int getEcControlPort() {
    return ecControlPort;
  }

  public void setEcControlPort(int value) {
    this.ecControlPort = value;
  }

}
