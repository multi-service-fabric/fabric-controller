package msf.fc.node.nodes.leafs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class LeafNodeReadListResponseBody extends AbstractResponseBody {

  @SerializedName("leaf_node_ids")
  private List<String> leafNodeIdList;

  public List<String> getLeafNodeIdList() {
    return leafNodeIdList;
  }

  public void setLeafNodeIdList(List<String> leafNodeIdList) {
    this.leafNodeIdList = leafNodeIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
