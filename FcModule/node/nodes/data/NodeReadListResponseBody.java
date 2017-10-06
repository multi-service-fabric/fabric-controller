package msf.fc.node.nodes.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class NodeReadListResponseBody extends AbstractResponseBody {

  @SerializedName("leaf_node_ids")
  private List<String> leafNodeIdList;

  @SerializedName("spine_node_ids")
  private List<String> spineNodeIdList;

  @SerializedName("rr_node_ids")
  private List<String> rrNodeIdList;

  public List<String> getLeafNodeIdList() {
    return leafNodeIdList;
  }

  public void setLeafNodeIdList(List<String> leafNodeIdList) {
    this.leafNodeIdList = leafNodeIdList;
  }

  public List<String> getSpineNodeIdList() {
    return spineNodeIdList;
  }

  public void setSpineNodeIdList(List<String> spineNodeIdList) {
    this.spineNodeIdList = spineNodeIdList;
  }

  public List<String> getRrNodeIdList() {
    return rrNodeIdList;
  }

  public void setRrNodeIdList(List<String> rrNodeIdList) {
    this.rrNodeIdList = rrNodeIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
