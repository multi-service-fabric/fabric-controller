
package msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OspfNeighborIfListEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
