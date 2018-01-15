
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LeafRr", propOrder = { "leafRrSwClusterId", "leafRrNodeId", "leafRrRouterId" })
public class LeafRr {

  @XmlSchemaType(name = "integer")
  protected int leafRrSwClusterId;
  @XmlSchemaType(name = "integer")
  protected int leafRrNodeId;
  @XmlElement(required = true)
  protected String leafRrRouterId;

  public int getLeafRrSwClusterId() {
    return leafRrSwClusterId;
  }

  public void setLeafRrSwClusterId(int value) {
    this.leafRrSwClusterId = value;
  }

  public int getLeafRrNodeId() {
    return leafRrNodeId;
  }

  public void setLeafRrNodeId(int value) {
    this.leafRrNodeId = value;
  }

  public String getLeafRrRouterId() {
    return leafRrRouterId;
  }

  public void setLeafRrRouterId(String value) {
    this.leafRrRouterId = value;
  }

}
