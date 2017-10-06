package msf.fc.rest.ec.node.nodes.leaf.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.nodes.leaf.data.entity.LeafEcEntity;

public class LeafNodeReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("leafs")
  private List<LeafEcEntity> leafList;

  public List<LeafEcEntity> getLeafList() {
    return leafList;
  }

  public void setLeafList(List<LeafEcEntity> leafList) {
    this.leafList = leafList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
