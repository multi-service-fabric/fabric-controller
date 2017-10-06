
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemConf", propOrder = {})
public class SystemConf {

  @XmlElement(required = true)
  protected Rest rest;
  @XmlElement(required = true)
  protected Slice slice;
  @XmlElement(required = true)
  protected SwClustersData swClustersData;
  @XmlElement(required = true)
  protected Traffic traffic;

  public Rest getRest() {
    return rest;
  }

  public void setRest(Rest value) {
    this.rest = value;
  }

  public Slice getSlice() {
    return slice;
  }

  public void setSlice(Slice value) {
    this.slice = value;
  }

  public SwClustersData getSwClustersData() {
    return swClustersData;
  }

  public void setSwClustersData(SwClustersData value) {
    this.swClustersData = value;
  }

  public Traffic getTraffic() {
    return traffic;
  }

  public void setTraffic(Traffic value) {
    this.traffic = value;
  }

}
