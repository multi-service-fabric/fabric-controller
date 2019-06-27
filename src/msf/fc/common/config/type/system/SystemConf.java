
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemConf", propOrder = {})
public class SystemConf {

  @XmlElement(required = true)
  protected Controller controller;
  @XmlElement(required = true)
  protected Rest rest;
  @XmlElement(required = true)
  protected Slice slice;
  @XmlElement(required = true)
  protected SwClustersData swClustersData;
  @XmlElement(required = true)
  protected Status status;
  @XmlElement(required = true)
  protected Failure failure;
  @XmlElement(required = true)
  protected Traffic traffic;
  @XmlElement(required = true)
  protected Qos qos;
  @XmlElement(required = true)
  protected Irb irb;
  @XmlElement(required = true)
  protected Node node;

  public Controller getController() {
    return controller;
  }

  public void setController(Controller value) {
    this.controller = value;
  }

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status value) {
    this.status = value;
  }

  public Failure getFailure() {
    return failure;
  }

  public void setFailure(Failure value) {
    this.failure = value;
  }

  public Traffic getTraffic() {
    return traffic;
  }

  public void setTraffic(Traffic value) {
    this.traffic = value;
  }

  public Qos getQos() {
    return qos;
  }

  public void setQos(Qos value) {
    this.qos = value;
  }

  public Irb getIrb() {
    return irb;
  }

  public void setIrb(Irb value) {
    this.irb = value;
  }

  public Node getNode() {
    return node;
  }

  public void setNode(Node value) {
    this.node = value;
  }
}
