
package msf.fc.common.config.type.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rrs", propOrder = { "rr" })
public class Rrs {

  @XmlElement(required = true)
  protected List<Rr> rr;

  public List<Rr> getRr() {
    if (rr == null) {
      rr = new ArrayList<Rr>();
    }
    return this.rr;
  }

}
