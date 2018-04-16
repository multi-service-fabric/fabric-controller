
package msf.mfcfc.node.nodes.spines.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class SpineNodeReadListResponseBody extends AbstractResponseBody {

  @SerializedName("spine_node_ids")
  private List<String> spineNodeIdList;

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
