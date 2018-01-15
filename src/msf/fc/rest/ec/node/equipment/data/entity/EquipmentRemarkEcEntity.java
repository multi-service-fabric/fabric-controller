package msf.fc.rest.ec.node.equipment.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class EquipmentRemarkEcEntity {

  
  @SerializedName("enable")
  private Boolean enable;

  
  @SerializedName("menu")
  private List<String> menuList;

  
  @SerializedName("default")
  private String remarkDefault;

  
  public Boolean getEnable() {
    return enable;
  }

  
  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  
  public List<String> getMenuList() {
    return menuList;
  }

  
  public void setMenuList(List<String> menuList) {
    this.menuList = menuList;
  }

  
  public String getRemarkDefault() {
    return remarkDefault;
  }

  
  public void setRemarkDefault(String remarkDefault) {
    this.remarkDefault = remarkDefault;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
