package msf.fc.node.nodes.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.nodes.leafs.data.entity.LeafEntity;
import msf.fc.node.nodes.rrs.data.entity.RrEntity;
import msf.fc.node.nodes.spines.data.entity.SpineEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class NodeReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("leafs")
  private List<LeafEntity> leafEntityList;

  @SerializedName("spines")
  private List<SpineEntity> spineEntityList;

  @SerializedName("rrs")
  private List<RrEntity> rrEntityList;

  public List<LeafEntity> getLeafEntityList() {
    return leafEntityList;
  }

  public void setLeafEntityList(List<LeafEntity> leafEntityList) {
    this.leafEntityList = leafEntityList;
  }

  public List<SpineEntity> getSpineEntityList() {
    return spineEntityList;
  }

  public void setSpineEntityList(List<SpineEntity> spineEntityList) {
    this.spineEntityList = spineEntityList;
  }

  public List<RrEntity> getRrEntityList() {
    return rrEntityList;
  }

  public void setRrEntityList(List<RrEntity> rrEntityList) {
    this.rrEntityList = rrEntityList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
