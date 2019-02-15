
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationL3VniEcEntity {

  @SerializedName("vni_id")
  private Integer vniId;

  @SerializedName("vlan_id")
  private Integer vlanId;

  public Integer getVniId() {
    return vniId;
  }

  public void setVniId(Integer vniId) {
    this.vniId = vniId;
  }

  public Integer getVlanId() {
    return vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
