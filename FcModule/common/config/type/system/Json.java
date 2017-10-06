
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Json", propOrder = {})
public class Json {

  protected boolean isPrettyPrinting;
  protected boolean isSerializeNulls;

  public boolean isPrettyPrinting() {
    return isPrettyPrinting;
  }

  public void setIsPrettyPrinting(boolean value) {
    this.isPrettyPrinting = value;
  }

  public boolean isSerializeNulls() {
    return isSerializeNulls;
  }

  public void setIsSerializeNulls(boolean value) {
    this.isSerializeNulls = value;
  }

}
