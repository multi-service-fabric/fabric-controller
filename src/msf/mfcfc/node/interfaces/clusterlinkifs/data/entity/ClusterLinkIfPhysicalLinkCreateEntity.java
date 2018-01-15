
package msf.mfcfc.node.interfaces.clusterlinkifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ClusterLinkIfPhysicalLinkCreateEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("physical_if_id")
  private String physicalIfId;

  
  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  
  @SerializedName("opposite_node_id")
  private String oppositeNodeId;

  
  @SerializedName("opposite_if_id")
  private String oppositeIfId;

  
  @SerializedName("opposite_breakout_if_id")
  private String oppositeBreakoutIfId;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  
  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  
  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  
  public String getOppositeNodeId() {
    return oppositeNodeId;
  }

  
  public void setOppositeNodeId(String oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  
  public String getOppositeIfId() {
    return oppositeIfId;
  }

  
  public void setOppositeIfId(String oppositeIfId) {
    this.oppositeIfId = oppositeIfId;
  }

  
  public String getOppositeBreakoutIfId() {
    return oppositeBreakoutIfId;
  }

  
  public void setOppositeBreakoutIfId(String oppositeBreakoutIfId) {
    this.oppositeBreakoutIfId = oppositeBreakoutIfId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
