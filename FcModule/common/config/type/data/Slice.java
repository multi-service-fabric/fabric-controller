
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Slice", propOrder = { "ipv4MulticastAddressBase" })
public class Slice {

  @XmlElement(required = true)
  protected String ipv4MulticastAddressBase;

  public String getIpv4MulticastAddressBase() {
    return ipv4MulticastAddressBase;
  }

  public void setIpv4MulticastAddressBase(String value) {
    this.ipv4MulticastAddressBase = value;
  }

}
