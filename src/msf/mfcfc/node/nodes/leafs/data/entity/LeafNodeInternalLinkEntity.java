package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LeafNodeInternalLinkEntity {

  
  @SerializedName("physical_links")
  private List<LeafNodePhysicalLinkEntity> physicalLinkList;

  
  @SerializedName("lag_links")
  private List<LeafNodeLagLinkEntity> lagLinkList;

  
  public List<LeafNodePhysicalLinkEntity> getPhysicalLinkList() {
    return physicalLinkList;
  }

  
  public void setPhysicalLinkList(List<LeafNodePhysicalLinkEntity> physicalLinkList) {
    this.physicalLinkList = physicalLinkList;
  }

  
  public List<LeafNodeLagLinkEntity> getLagLinkList() {
    return lagLinkList;
  }

  
  public void setLagLinkList(List<LeafNodeLagLinkEntity> lagLinkList) {
    this.lagLinkList = lagLinkList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
