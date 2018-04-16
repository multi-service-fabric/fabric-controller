
package msf.fc.rest.ec.node.equipment.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentEgressEcEntity {

  @SerializedName("menu")
  private List<String> menuList;

  @SerializedName("default")
  private String egressDefault;

  public List<String> getMenuList() {
    return menuList;
  }

  public void setMenuList(List<String> menuList) {
    this.menuList = menuList;
  }

  public String getEgressDefault() {
    return egressDefault;
  }

  public void setEgressDefault(String egressDefault) {
    this.egressDefault = egressDefault;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
