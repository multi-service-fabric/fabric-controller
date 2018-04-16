
package msf.mfc.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwClustersData", propOrder = { "swClusterData" })
public class SwClustersData {

  @XmlElement(required = true)
  protected List<SwClusterData> swClusterData;

  public List<SwClusterData> getSwClusterData() {
    if (swClusterData == null) {
      swClusterData = new ArrayList<SwClusterData>();
    }
    return this.swClusterData;
  }

}
