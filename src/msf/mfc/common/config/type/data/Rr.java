
package msf.mfc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rr", propOrder = {})
public class Rr {

  @XmlSchemaType(name = "integer")
  protected int rrNodeId;

  public int getRrNodeId() {
    return rrNodeId;
  }

  public void setRrNodeId(int value) {
    this.rrNodeId = value;
  }

}
