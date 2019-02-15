
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentCapabilityEntity {

  @SerializedName("vpn")
  private EquipmentVpnEntity vpn;

  @SerializedName("qos")
  private EquipmentQosEntity qos;

  @SerializedName("irb")
  private EquipmentIrbEntity irb;

  @SerializedName("traffic")
  private EquipmentTrafficEntity traffic;

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

  public EquipmentIrbEntity getIrb() {
    return irb;
  }

  public void setIrb(EquipmentIrbEntity irb) {
    this.irb = irb;
  }

  public EquipmentTrafficEntity getTraffic() {
    return traffic;
  }

  public void setTraffic(EquipmentTrafficEntity traffic) {
    this.traffic = traffic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
