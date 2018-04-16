
package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationUpdateL3VlanIfOptionEcEntity {

  @SerializedName("vrf_id")
  private String vrfId;

  @SerializedName("vlan_ifs")
  private List<OperationVlanIfEcEntity> vlanIfList;

  @SerializedName("remark_menu")
  private String remarkMenu;

  public String getVrfId() {
    return vrfId;
  }

  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  public List<OperationVlanIfEcEntity> getVlanIfList() {
    return vlanIfList;
  }

  public void setVlanIfList(List<OperationVlanIfEcEntity> vlanIfList) {
    this.vlanIfList = vlanIfList;
  }

  public String getRemarkMenu() {
    return remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
