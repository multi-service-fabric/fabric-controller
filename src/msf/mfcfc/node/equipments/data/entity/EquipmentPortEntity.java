
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentPortEntity {

  @SerializedName("speed")
  private String speed;

  @SerializedName("port_prefix")
  private String portPrefix;

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public String getPortPrefix() {
    return portPrefix;
  }

  public void setPortPrefix(String portPrefix) {
    this.portPrefix = portPrefix;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
