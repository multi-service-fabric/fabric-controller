package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class VlanIfCapabilityEcEntity {

  
  @SerializedName("shaping")
  private Boolean shaping;

  
  @SerializedName("remark")
  private Boolean remark;

  
  @SerializedName("remark_menu_list")
  private List<String> remarkMenuList;

  
  @SerializedName("egress_menu_list")
  private List<String> egressMenuList;

  
  public Boolean getShaping() {
    return shaping;
  }

  
  public void setShaping(Boolean shaping) {
    this.shaping = shaping;
  }

  
  public Boolean getRemark() {
    return remark;
  }

  
  public void setRemark(Boolean remark) {
    this.remark = remark;
  }

  
  public List<String> getRemarkMenuList() {
    return remarkMenuList;
  }

  
  public void setRemarkMenuList(List<String> remarkMenuList) {
    this.remarkMenuList = remarkMenuList;
  }

  
  public List<String> getEgressMenuList() {
    return egressMenuList;
  }

  
  public void setEgressMenuList(List<String> egressMenuList) {
    this.egressMenuList = egressMenuList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
