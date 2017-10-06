package msf.fc.node.interfaces.edgepoints.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class BaseIfEntity {

  @SerializedName("leaf_node_id")
  private String leafNodeId;

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("physical_if_id")
  private String physicalIfId;

  public String getLeafNodeId() {
    return leafNodeId;
  }

  public void setLeafNodeId(String leafNodeId) {
    this.leafNodeId = leafNodeId;
  }

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
