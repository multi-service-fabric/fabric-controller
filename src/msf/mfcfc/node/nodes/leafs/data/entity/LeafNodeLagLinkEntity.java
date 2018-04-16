
package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeLagLinkEntity {

  @SerializedName("opposite_node_id")
  private String oppositeNodeId;

  @SerializedName("local_traffic_threshold")
  private Double localTrafficThreshold;

  @SerializedName("opposite_traffic_threshold")
  private Double oppositeTrafficThreshold;

  @SerializedName("member_ifs")
  private List<LeafNodeMemberIfEntity> memberIfList;

  public String getOppositeNodeId() {
    return oppositeNodeId;
  }

  public void setOppositeNodeId(String oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  public Double getLocalTrafficThreshold() {
    return localTrafficThreshold;
  }

  public void setLocalTrafficThreshold(Double localTrafficThreshold) {
    this.localTrafficThreshold = localTrafficThreshold;
  }

  public Double getOppositeTrafficThreshold() {
    return oppositeTrafficThreshold;
  }

  public void setOppositeTrafficThreshold(Double oppositeTrafficThreshold) {
    this.oppositeTrafficThreshold = oppositeTrafficThreshold;
  }

  public List<LeafNodeMemberIfEntity> getMemberIfList() {
    return memberIfList;
  }

  public void setMemberIfList(List<LeafNodeMemberIfEntity> memberIfList) {
    this.memberIfList = memberIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
