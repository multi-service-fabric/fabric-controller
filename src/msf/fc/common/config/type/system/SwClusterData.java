
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwClusterData", propOrder = {})
public class SwClusterData {

  @XmlElement(required = true)
  protected SwCluster swCluster;

  public SwCluster getSwCluster() {
    return swCluster;
  }

  public void setSwCluster(SwCluster value) {
    this.swCluster = value;
  }

}
