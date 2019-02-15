
package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationCreateUpdateL2VlanIfOptionEcEntity {

  @SerializedName("create_vlan_ifs")
  private List<OperationCreateVlanIfEcEntity> createVlanIfList;

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("vni")
  private Integer vni;

  @SerializedName("plane")
  private Integer plane;

  @SerializedName("l3_vni")
  private OperationL3VniEcEntity l3Vni;

  @SerializedName("loopback_interface")
  private OperationLoopbackInterfaceEcEntity loopbackInterface;

  @SerializedName("update_vlan_ifs")
  private List<OperationUpdateVlanIfEcEntity> updateVlanIfList;

  public List<OperationCreateVlanIfEcEntity> getCreateVlanIfList() {
    return createVlanIfList;
  }

  public void setCreateVlanIfList(List<OperationCreateVlanIfEcEntity> createVlanIfList) {
    this.createVlanIfList = createVlanIfList;
  }

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public Integer getVni() {
    return vni;
  }

  public void setVni(Integer vni) {
    this.vni = vni;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public OperationL3VniEcEntity getL3Vni() {
    return l3Vni;
  }

  public void setL3Vni(OperationL3VniEcEntity l3Vni) {
    this.l3Vni = l3Vni;
  }

  public OperationLoopbackInterfaceEcEntity getLoopbackInterface() {
    return loopbackInterface;
  }

  public void setLoopbackInterface(OperationLoopbackInterfaceEcEntity loopbackInterface) {
    this.loopbackInterface = loopbackInterface;
  }

  public List<OperationUpdateVlanIfEcEntity> getUpdateVlanIfList() {
    return updateVlanIfList;
  }

  public void setUpdateVlanIfList(List<OperationUpdateVlanIfEcEntity> updateVlanIfList) {
    this.updateVlanIfList = updateVlanIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
