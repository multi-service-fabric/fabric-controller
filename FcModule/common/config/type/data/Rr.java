
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rr", propOrder = {})
public class Rr {

  @XmlSchemaType(name = "integer")
  protected int rrNodeId;
  @XmlElement(required = true)
  protected String rrRouterId;

  public int getRrNodeId() {
    return rrNodeId;
  }

  public void setRrNodeId(int value) {
    this.rrNodeId = value;
  }

  public String getRrRouterId() {
    return rrRouterId;
  }

  public void setRrRouterId(String value) {
    this.rrRouterId = value;
  }

}
