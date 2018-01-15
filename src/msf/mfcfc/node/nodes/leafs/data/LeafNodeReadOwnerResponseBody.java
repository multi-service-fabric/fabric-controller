package msf.mfcfc.node.nodes.leafs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForOwnerEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class LeafNodeReadOwnerResponseBody extends AbstractResponseBody {

  
  @SerializedName("leaf")
  private LeafNodeForOwnerEntity leaf;

  
  public LeafNodeForOwnerEntity getLeaf() {
    return leaf;
  }

  
  public void setLeaf(LeafNodeForOwnerEntity leaf) {
    this.leaf = leaf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
