
package msf.fc.common.config.type.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rrs", propOrder = { "rr", "leafRr" })
public class Rrs {

  protected List<Rr> rr;
  protected List<LeafRr> leafRr;

  public List<Rr> getRr() {
    if (rr == null) {
      rr = new ArrayList<Rr>();
    }
    return this.rr;
  }

  public List<LeafRr> getLeafRr() {
    if (leafRr == null) {
      leafRr = new ArrayList<LeafRr>();
    }
    return this.leafRr;
  }

}
