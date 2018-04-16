
package msf.mfc.common.config.type.data;

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
  protected String inchannelStartAddress;

  public int getSwClusterId() {
    return swClusterId;
  }

  public void setSwClusterId(int value) {
    this.swClusterId = value;
  }

  public String getInchannelStartAddress() {
    return inchannelStartAddress;
  }

  public void setInchannelStartAddress(String value) {
    this.inchannelStartAddress = value;
  }

}
