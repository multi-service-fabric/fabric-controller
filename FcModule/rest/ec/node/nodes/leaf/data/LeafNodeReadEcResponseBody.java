package msf.fc.rest.ec.node.nodes.leaf.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.nodes.leaf.data.entity.LeafEcEntity;

public class LeafNodeReadEcResponseBody extends AbstractInternalResponseBody {
  @SerializedName("leaf")
  private LeafEcEntity leaf;

  public LeafEcEntity getLeaf() {
    return leaf;
  }

  public void setLeaf(LeafEcEntity leaf) {
    this.leaf = leaf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
