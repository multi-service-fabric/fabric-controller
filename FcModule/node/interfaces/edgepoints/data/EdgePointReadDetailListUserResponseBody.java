package msf.fc.node.interfaces.edgepoints.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.edgepoints.data.entity.L2EdgePointForUserEntity;
import msf.fc.node.interfaces.edgepoints.data.entity.L3EdgePointForUserEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class EdgePointReadDetailListUserResponseBody extends AbstractResponseBody {

  @SerializedName("l2_edge_points")
  private List<L2EdgePointForUserEntity> l2EdgePointList;

  @SerializedName("l3_edge_points")
  private List<L3EdgePointForUserEntity> l3EdgePointList;

  public List<L2EdgePointForUserEntity> getL2EdgePointList() {
    return l2EdgePointList;
  }

  public void setL2EdgePointList(List<L2EdgePointForUserEntity> l2EdgePointList) {
    this.l2EdgePointList = l2EdgePointList;
  }

  public List<L3EdgePointForUserEntity> getL3EdgePointList() {
    return l3EdgePointList;
  }

  public void setL3EdgePointList(List<L3EdgePointForUserEntity> l3EdgePointList) {
    this.l3EdgePointList = l3EdgePointList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
