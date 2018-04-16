
package msf.mfcfc.node.nodes.leafs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForOwnerEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class LeafNodeReadOwnerDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("leafs")
  private List<LeafNodeForOwnerEntity> leafList;

  public List<LeafNodeForOwnerEntity> getLeafList() {
    return leafList;
  }

  public void setLeafList(List<LeafNodeForOwnerEntity> leafList) {
    this.leafList = leafList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
