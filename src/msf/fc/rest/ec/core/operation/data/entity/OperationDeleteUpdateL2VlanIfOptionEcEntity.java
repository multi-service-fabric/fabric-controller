
package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationDeleteUpdateL2VlanIfOptionEcEntity {

  @SerializedName("vrf_id")
  private String vrfId;

  @SerializedName("delete_vlan_ifs")
  private List<OperationDeleteVlanIfEcEntity> deleteVlanIfList;

  @SerializedName("update_vlan_ifs")
  private List<OperationUpdateVlanIfEcEntity> updateVlanIfList;

  public String getVrfId() {
    return vrfId;
  }

  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  public List<OperationDeleteVlanIfEcEntity> getDeleteVlanIfList() {
    return deleteVlanIfList;
  }

  public void setDeleteVlanIfList(List<OperationDeleteVlanIfEcEntity> deleteVlanIfList) {
    this.deleteVlanIfList = deleteVlanIfList;
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
