
package msf.fc.common.config.type.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwCluster", propOrder = {})
public class SwCluster {

  @XmlSchemaType(name = "integer")
  protected int swClusterId;
  @XmlSchemaType(name = "integer")
  protected int maxLeafNum;
  @XmlSchemaType(name = "integer")
  protected int maxSpineNum;
  @XmlSchemaType(name = "integer")
  protected int maxRrNum;
  @XmlSchemaType(name = "integer")
  protected int spineStartPos;
  @XmlSchemaType(name = "integer")
  protected int leafStartPos;
  @XmlSchemaType(name = "integer")
  protected int rrStartPos;
  @XmlSchemaType(name = "integer")
  protected int fcStartPos;
  @XmlSchemaType(name = "integer")
  protected int ecStartPos;
  @XmlSchemaType(name = "integer")
  protected int emStartPos;
  @XmlSchemaType(name = "integer")
  protected int asNum;
  @XmlSchemaType(name = "integer")
  protected int ospfArea;
  @XmlElement(required = true)
  protected String inchannelStartAddress;
  @XmlElement(required = true)
  protected String outchannelStartAddress;
  @XmlElement(required = true)
  protected String aggrigationStartAddress;
  @XmlSchemaType(name = "integer")
  protected int aggrigationAddressPrefix;

  public int getSwClusterId() {
    return swClusterId;
  }

  public void setSwClusterId(int value) {
    this.swClusterId = value;
  }

  public int getMaxLeafNum() {
    return maxLeafNum;
  }

  public void setMaxLeafNum(int value) {
    this.maxLeafNum = value;
  }

  public int getMaxSpineNum() {
    return maxSpineNum;
  }

  public void setMaxSpineNum(int value) {
    this.maxSpineNum = value;
  }

  public int getMaxRrNum() {
    return maxRrNum;
  }

  public void setMaxRrNum(int value) {
    this.maxRrNum = value;
  }

  public int getSpineStartPos() {
    return spineStartPos;
  }

  public void setSpineStartPos(int value) {
    this.spineStartPos = value;
  }

  public int getLeafStartPos() {
    return leafStartPos;
  }

  public void setLeafStartPos(int value) {
    this.leafStartPos = value;
  }

  public int getRrStartPos() {
    return rrStartPos;
  }

  public void setRrStartPos(int value) {
    this.rrStartPos = value;
  }

  public int getFcStartPos() {
    return fcStartPos;
  }

  public void setFcStartPos(int value) {
    this.fcStartPos = value;
  }

  public int getEcStartPos() {
    return ecStartPos;
  }

  public void setEcStartPos(int value) {
    this.ecStartPos = value;
  }

  public int getEmStartPos() {
    return emStartPos;
  }

  public void setEmStartPos(int value) {
    this.emStartPos = value;
  }

  public int getAsNum() {
    return asNum;
  }

  public void setAsNum(int value) {
    this.asNum = value;
  }

  public int getOspfArea() {
    return ospfArea;
  }

  public void setOspfArea(int value) {
    this.ospfArea = value;
  }

  public String getInchannelStartAddress() {
    return inchannelStartAddress;
  }

  public void setInchannelStartAddress(String value) {
    this.inchannelStartAddress = value;
  }

  public String getOutchannelStartAddress() {
    return outchannelStartAddress;
  }

  public void setOutchannelStartAddress(String value) {
    this.outchannelStartAddress = value;
  }

  public String getAggrigationStartAddress() {
    return aggrigationStartAddress;
  }

  public void setAggrigationStartAddress(String value) {
    this.aggrigationStartAddress = value;
  }

  public int getAggrigationAddressPrefix() {
    return aggrigationAddressPrefix;
  }

  public void setAggrigationAddressPrefix(int value) {
    this.aggrigationAddressPrefix = value;
  }

}
