
package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VlanIfL3VlanOptionEcEntity {

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("static_routes")
  private List<VlanIfStaticRouteUpdateEcEntity> staticRouteList;

  @SerializedName("qos")
  private VlanIfQosUpdateEcEntity qos;

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public List<VlanIfStaticRouteUpdateEcEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<VlanIfStaticRouteUpdateEcEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

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
