
package msf.mfcfc.node.interfaces.edgepoints.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class EdgePointReadListResponseBody extends AbstractResponseBody {

  @SerializedName("edge_point_ids")
  private List<String> edgePointIdList;

  public List<String> getEdgePointIdList() {
    return edgePointIdList;
  }

  public void setEdgePointIdList(List<String> edgePointIdList) {
    this.edgePointIdList = edgePointIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
