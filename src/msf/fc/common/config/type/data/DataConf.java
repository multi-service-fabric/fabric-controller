
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataConf", propOrder = {})
public class DataConf {

  @XmlElement(required = true)
  protected SwClustersData swClustersData;

  public SwClustersData getSwClustersData() {
    return swClustersData;
  }

  public void setSwClustersData(SwClustersData value) {
    this.swClustersData = value;
  }

}
