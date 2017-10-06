package msf.fc.node.clusters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EdgePointEntity {


  @SerializedName("l2_edge_points")
  private List<String> l2EdgePointList;


  @SerializedName("l3_edge_points")
  private List<String> l3EdgePointList;

  public List<String> getL2EdgePointList() {
    return l2EdgePointList;
  }

  public void setL2EdgePointList(List<String> l2EdgePointList) {
    this.l2EdgePointList = l2EdgePointList;
  }

  public List<String> getL3EdgePointList() {
    return l3EdgePointList;
  }

  public void setL3EdgePointList(List<String> l3EdgePointList) {
    this.l3EdgePointList = l3EdgePointList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
