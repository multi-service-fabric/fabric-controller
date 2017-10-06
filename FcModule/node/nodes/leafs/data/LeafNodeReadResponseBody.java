package msf.fc.node.nodes.leafs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.node.nodes.leafs.data.entity.LeafEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class LeafNodeReadResponseBody extends AbstractResponseBody {

  private LeafEntity leaf;

  public LeafEntity getLeaf() {
    return leaf;
  }

  public void setLeaf(LeafEntity leaf) {
    this.leaf = leaf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
