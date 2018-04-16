
package msf.mfcfc.node.equipments.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentIfDefinitionEntity {

  @SerializedName("ports")
  private List<EquipmentPortEntity> portList;

  @SerializedName("lag_prefix")
  private String lagPrefix;

  @SerializedName("unit_connector")
  private String unitConnector;

  public List<EquipmentPortEntity> getPortList() {
    return portList;
  }

  public void setPortList(List<EquipmentPortEntity> portList) {
    this.portList = portList;
  }

  public String getLagPrefix() {
    return lagPrefix;
  }

  public void setLagPrefix(String lagPrefix) {
    this.lagPrefix = lagPrefix;
  }

  public String getUnitConnector() {
    return unitConnector;
  }

  public void setUnitConnector(String unitConnector) {
    this.unitConnector = unitConnector;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
