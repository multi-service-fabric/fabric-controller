
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Irb", propOrder = { "l3VniVlanIdStartPos", "l3VniVlanIdEndPos" })
public class Irb {

  @XmlSchemaType(name = "integer")
  protected int l3VniVlanIdStartPos;
  @XmlSchemaType(name = "integer")
  protected int l3VniVlanIdEndPos;

  public int getL3VniVlanIdStartPos() {
    return l3VniVlanIdStartPos;
  }

  public void setL3VniVlanIdStartPos(int value) {
    this.l3VniVlanIdStartPos = value;
  }

  public int getL3VniVlanIdEndPos() {
    return l3VniVlanIdEndPos;
  }

  public void setL3VniVlanIdEndPos(int value) {
    this.l3VniVlanIdEndPos = value;
  }

}
