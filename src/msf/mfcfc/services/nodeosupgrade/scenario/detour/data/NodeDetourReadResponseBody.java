
package msf.mfcfc.services.nodeosupgrade.scenario.detour.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class NodeDetourReadResponseBody extends AbstractResponseBody {

  @SerializedName("leaf_node_ids")
  private List<String> leafNodeIdList;

  @SerializedName("spine_node_ids")
  private List<String> spineNodeIdList;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
