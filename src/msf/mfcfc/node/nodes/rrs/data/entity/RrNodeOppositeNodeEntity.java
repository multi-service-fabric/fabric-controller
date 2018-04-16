
package msf.mfcfc.node.nodes.rrs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class RrNodeOppositeNodeEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("maintenance_if_id")
  private String maintenanceIfId;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getMaintenanceIfId() {
    return maintenanceIfId;
  }

  public void setMaintenanceIfId(String maintenanceIfId) {
    this.maintenanceIfId = maintenanceIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
