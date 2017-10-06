
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
  protected int asNum;
  @XmlElement(required = true)
  protected String rpLoopbackAddress;
  @XmlElement(required = true)
  protected String interfaceStartAddress;
  @XmlElement(required = true)
  protected String loopbackStartAddress;
  @XmlElement(required = true)
  protected String managementStartAddress;
  @XmlSchemaType(name = "integer")
  protected int managementAddressPrefix;

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

  public int getAsNum() {
    return asNum;
  }

  public void setAsNum(int value) {
    this.asNum = value;
  }

  public String getRpLoopbackAddress() {
    return rpLoopbackAddress;
  }

  public void setRpLoopbackAddress(String value) {
    this.rpLoopbackAddress = value;
  }

  public String getInterfaceStartAddress() {
    return interfaceStartAddress;
  }

  public void setInterfaceStartAddress(String value) {
    this.interfaceStartAddress = value;
  }

  public String getLoopbackStartAddress() {
    return loopbackStartAddress;
  }

  public void setLoopbackStartAddress(String value) {
    this.loopbackStartAddress = value;
  }

  public String getManagementStartAddress() {
    return managementStartAddress;
  }

  public void setManagementStartAddress(String value) {
    this.managementStartAddress = value;
  }

  public int getManagementAddressPrefix() {
    return managementAddressPrefix;
  }

  public void setManagementAddressPrefix(int value) {
    this.managementAddressPrefix = value;
  }

}
