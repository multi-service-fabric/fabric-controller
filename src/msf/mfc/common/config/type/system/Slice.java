
package msf.mfc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Slice", propOrder = {})
public class Slice {

  @XmlSchemaType(name = "integer")
  protected int l2SlicesMagnificationNum;
  @XmlSchemaType(name = "integer")
  protected int l3SlicesMagnificationNum;
  @XmlSchemaType(name = "integer")
  protected int l2MaxSlicesNum;
  @XmlSchemaType(name = "integer")
  protected int l3MaxSlicesNum;

  public int getL2SlicesMagnificationNum() {
    return l2SlicesMagnificationNum;
  }

  public void setL2SlicesMagnificationNum(int value) {
    this.l2SlicesMagnificationNum = value;
  }

  public int getL3SlicesMagnificationNum() {
    return l3SlicesMagnificationNum;
  }

  public void setL3SlicesMagnificationNum(int value) {
    this.l3SlicesMagnificationNum = value;
  }

  public int getL2MaxSlicesNum() {
    return l2MaxSlicesNum;
  }

  public void setL2MaxSlicesNum(int value) {
    this.l2MaxSlicesNum = value;
  }

  public int getL3MaxSlicesNum() {
    return l3MaxSlicesNum;
  }

  public void setL3MaxSlicesNum(int value) {
    this.l3MaxSlicesNum = value;
  }

}
