
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeNeighborEcEntity {

  @SerializedName("addresses")
  private List<String> addressList;

  public List<String> getAddressList() {
    return addressList;
  }

  public void setAddressList(List<String> addressList) {
    this.addressList = addressList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
