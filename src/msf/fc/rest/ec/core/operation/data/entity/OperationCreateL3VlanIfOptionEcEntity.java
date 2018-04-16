
package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationCreateL3VlanIfOptionEcEntity {

  @SerializedName("vlan_ifs")
  private List<OperationVlanIfCreateEcEntity> vlanIfList;

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("plane")
  private Integer plane;

  public List<OperationVlanIfCreateEcEntity> getVlanIfList() {
    return vlanIfList;
  }

  public void setVlanIfList(List<OperationVlanIfCreateEcEntity> vlanIfList) {
    this.vlanIfList = vlanIfList;
  }

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
