package msf.fc.node.interfaces.edgepoints.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class EdgePointReadListResponseBody extends AbstractResponseBody {

  @SerializedName("l2_edge_point_ids")
  private List<String> l2EdgePointIdList;

  @SerializedName("l3_edge_point_ids")
  private List<String> l3EdgePointIdList;

  public List<String> getL2EdgePointIdList() {
    return l2EdgePointIdList;
  }

  public void setL2EdgePointIdList(List<String> l2EdgePointIdList) {
    this.l2EdgePointIdList = l2EdgePointIdList;
  }

  public List<String> getL3EdgePointIdList() {
    return l3EdgePointIdList;
  }

  public void setL3EdgePointIdList(List<String> l3EdgePointIdList) {
    this.l3EdgePointIdList = l3EdgePointIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
