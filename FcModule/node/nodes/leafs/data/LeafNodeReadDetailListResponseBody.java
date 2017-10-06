package msf.fc.node.nodes.leafs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.nodes.leafs.data.entity.LeafEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class LeafNodeReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("leafs")
  private List<LeafEntity> leafList;

  public List<LeafEntity> getLeafList() {
    return leafList;
  }

  public void setLeafList(List<LeafEntity> leafList) {
    this.leafList = leafList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
