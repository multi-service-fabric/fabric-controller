
package msf.mfcfc.node.nodes.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForOwnerEntity;
import msf.mfcfc.node.nodes.rrs.data.entity.RrNodeRrEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class NodeReadOwnerDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("leafs")
  private List<LeafNodeForOwnerEntity> leafList;

  @SerializedName("spines")
  private List<SpineNodeEntity> spineList;

  @SerializedName("rrs")
  private List<RrNodeRrEntity> rrList;

  public List<LeafNodeForOwnerEntity> getLeafList() {
    return leafList;
  }

  public void setLeafList(List<LeafNodeForOwnerEntity> leafList) {
    this.leafList = leafList;
  }

  public List<SpineNodeEntity> getSpineList() {
    return spineList;
  }

  public void setSpineList(List<SpineNodeEntity> spineList) {
    this.spineList = spineList;
  }

  public List<RrNodeRrEntity> getRrList() {
    return rrList;
  }

  public void setRrList(List<RrNodeRrEntity> rrList) {
    this.rrList = rrList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
