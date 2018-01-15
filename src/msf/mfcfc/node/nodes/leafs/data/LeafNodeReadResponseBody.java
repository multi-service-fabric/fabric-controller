package msf.mfcfc.node.nodes.leafs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForUserEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class LeafNodeReadResponseBody extends AbstractResponseBody {

  
  @SerializedName("leaf")
  private LeafNodeForUserEntity leaf;

  
  public LeafNodeForUserEntity getLeaf() {
    return leaf;
  }

  
  public void setLeaf(LeafNodeForUserEntity leaf) {
    this.leaf = leaf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
