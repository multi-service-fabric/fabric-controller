
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Node", propOrder = { "lagIfIdStartPos" })
public class Node {

  @XmlSchemaType(name = "integer")
  protected int lagIfIdStartPos;

  public int getLagIfIdStartPos() {
    return lagIfIdStartPos;
  }

  public void setLagIfIdStartPos(int value) {
    this.lagIfIdStartPos = value;
  }

}
