
package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationCreateUpdateL2VlanIfOptionEcEntity {

  @SerializedName("create_vlan_ifs")
  private List<OperationCreateVlanIfEcEntity> createVlanIfList;

  @SerializedName("vrf_id")
  private Integer vrfId;

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
