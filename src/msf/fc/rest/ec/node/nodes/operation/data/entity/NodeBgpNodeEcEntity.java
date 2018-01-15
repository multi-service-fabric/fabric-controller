package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeBgpNodeEcEntity {

  
  @SerializedName("neighbor")
  private NodeNeighborEcEntity neighbor;

  
  @SerializedName("community")
  private String community;

  
  @SerializedName("community_wildcard")
  private String communityWildcard;

  
  public NodeNeighborEcEntity getNeighbor() {
    return neighbor;
  }

  
  public void setNeighbor(NodeNeighborEcEntity neighbor) {
    this.neighbor = neighbor;
  }

  
  public String getCommunity() {
    return community;
  }

  
  public void setCommunity(String community) {
    this.community = community;
  }

  
  public String getCommunityWildcard() {
    return communityWildcard;
  }

  
  public void setCommunityWildcard(String communityWildcard) {
    this.communityWildcard = communityWildcard;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
