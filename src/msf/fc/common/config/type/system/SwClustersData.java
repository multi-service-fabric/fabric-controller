
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwClustersData", propOrder = { "swClusterData" })
public class SwClustersData {

  @XmlElement(required = true)
  protected SwClusterData swClusterData;

  public SwClusterData getSwClusterData() {
    return swClusterData;
  }

  public void setSwClusterData(SwClusterData value) {
    this.swClusterData = value;
  }

}
