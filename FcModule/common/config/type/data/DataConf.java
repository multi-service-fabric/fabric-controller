
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataConf", propOrder = {})
public class DataConf {

  @XmlElement(required = true)
  protected SwClustersData swClustersData;
  @XmlElement(required = true)
  protected Slice slice;

  public SwClustersData getSwClustersData() {
    return swClustersData;
  }

  public void setSwClustersData(SwClustersData value) {
    this.swClustersData = value;
  }

  public Slice getSlice() {
    return slice;
  }

  public void setSlice(Slice value) {
    this.slice = value;
  }

}
