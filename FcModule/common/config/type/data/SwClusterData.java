
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwClusterData", propOrder = {})
public class SwClusterData {

  @XmlElement(required = true)
  protected SwCluster swCluster;
  @XmlElement(required = true)
  protected Rrs rrs;

  public SwCluster getSwCluster() {
    return swCluster;
  }

  public void setSwCluster(SwCluster value) {
    this.swCluster = value;
  }

  public Rrs getRrs() {
    return rrs;
  }

  public void setRrs(Rrs value) {
    this.rrs = value;
  }

}
