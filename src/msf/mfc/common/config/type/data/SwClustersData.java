
package msf.mfc.common.config.type.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwClustersData", propOrder = { "swClusterData", "maxSwClusterNum", "clusterStartAddress" })
public class SwClustersData {

  @XmlElement(required = true)
  protected List<SwClusterData> swClusterData;
  @XmlSchemaType(name = "integer")
  protected int maxSwClusterNum;
  @XmlElement(required = true)
  protected String clusterStartAddress;

  public List<SwClusterData> getSwClusterData() {
    if (swClusterData == null) {
      swClusterData = new ArrayList<SwClusterData>();
    }
    return this.swClusterData;
  }

  public int getMaxSwClusterNum() {
    return maxSwClusterNum;
  }

  public void setMaxSwClusterNum(int value) {
    this.maxSwClusterNum = value;
  }

  public String getClusterStartAddress() {
    return clusterStartAddress;
  }

  public void setClusterStartAddress(String value) {
    this.clusterStartAddress = value;
  }

}
