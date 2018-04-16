
package msf.fc.rest.ec.node.interfaces.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationClusterLinkEcEntity {

  @SerializedName("cost")
  private Integer cost;

  @SerializedName("address")
  private String address;

  @SerializedName("prefix")
  private Integer prefix;

  @SerializedName("condition")
  private String condition;

  public Integer getCost() {
    return cost;
  }

  public void setCost(Integer cost) {
    this.cost = cost;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPrefix() {
    return prefix;
  }

  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
