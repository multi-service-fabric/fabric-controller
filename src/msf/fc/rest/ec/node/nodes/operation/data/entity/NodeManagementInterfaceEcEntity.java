
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeManagementInterfaceEcEntity {

  @SerializedName("address")
  private String address;

  @SerializedName("prefix")
  private Integer prefix;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
