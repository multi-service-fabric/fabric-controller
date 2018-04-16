
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentCapabilityEntity {

  @SerializedName("vpn")
  private EquipmentVpnEntity vpn;

  @SerializedName("qos")
  private EquipmentQosEntity qos;

  public EquipmentVpnEntity getVpn() {
    return vpn;
  }

  public void setVpn(EquipmentVpnEntity vpn) {
    this.vpn = vpn;
  }

  public EquipmentQosEntity getQos() {
    return qos;
  }

  public void setQos(EquipmentQosEntity qos) {
    this.qos = qos;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
