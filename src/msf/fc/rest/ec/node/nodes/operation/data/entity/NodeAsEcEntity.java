
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeAsEcEntity {

  @SerializedName("as_number")
  private String asNumber;

  public String getAsNumber() {
    return asNumber;
  }

  public void setAsNumber(String asNumber) {
    this.asNumber = asNumber;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
