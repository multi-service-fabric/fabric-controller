package msf.fc.node.interfaces.edgepoints.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.edgepoints.data.entity.L2EdgePointForOwnerEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.L3EdgePointForOwnerEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class EdgePointReadDetailListOwnerResponseBody extends AbstractResponseBody {

  @SerializedName("l2_edge_points")
  private List<L2EdgePointForOwnerEntity> l2EdgePointList;

  @SerializedName("l3_edge_points")
  private List<L3EdgePointForOwnerEntity> l3EdgePointList;

  public List<L2EdgePointForOwnerEntity> getL2EdgePointList() {
    return l2EdgePointList;
  }

  public void setL2EdgePointList(List<L2EdgePointForOwnerEntity> l2EdgePointList) {
    this.l2EdgePointList = l2EdgePointList;
  }

  public List<L3EdgePointForOwnerEntity> getL3EdgePointList() {
    return l3EdgePointList;
  }

  public void setL3EdgePointList(List<L3EdgePointForOwnerEntity> l3EdgePointList) {
    this.l3EdgePointList = l3EdgePointList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
