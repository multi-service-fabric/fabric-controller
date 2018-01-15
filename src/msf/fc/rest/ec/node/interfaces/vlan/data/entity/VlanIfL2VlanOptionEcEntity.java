package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class VlanIfL2VlanOptionEcEntity {

  
  @SerializedName("qos")
  private VlanIfQosUpdateEcEntity qos;

  
  public VlanIfQosUpdateEcEntity getQos() {
    return qos;
  }

  
  public void setQos(VlanIfQosUpdateEcEntity qos) {
    this.qos = qos;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
