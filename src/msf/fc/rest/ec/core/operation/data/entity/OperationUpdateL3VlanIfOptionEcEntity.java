package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class OperationUpdateL3VlanIfOptionEcEntity {

  
  @SerializedName("slice_id")
  private String sliceId;

  
  @SerializedName("vlan_ifs")
  private List<OperationVlanIfEcEntity> vlanIfList;

  
  @SerializedName("remark_menu")
  private String remarkMenu;

  
  public String getSliceId() {
    return sliceId;
  }

  
  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
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
