
package msf.mfcfc.node.nodes.leafs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForUserEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class LeafNodeReadUserDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("leafs")
  private List<LeafNodeForUserEntity> leafList;

  public List<LeafNodeForUserEntity> getLeafList() {
    return leafList;
  }

  public void setLeafList(List<LeafNodeForUserEntity> leafList) {
    this.leafList = leafList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
