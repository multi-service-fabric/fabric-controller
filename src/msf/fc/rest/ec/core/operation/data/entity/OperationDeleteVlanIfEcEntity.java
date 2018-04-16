
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationDeleteVlanIfEcEntity {

  @SerializedName("vlan_if_id")
  private String vlanIfId;

  @SerializedName("node_id")
  private String nodeId;

  public String getVlanIfId() {
    return vlanIfId;
  }

  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
