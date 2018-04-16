
package msf.mfcfc.node.interfaces.edgepoints.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForUserEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class EdgePointReadDetailListUserResponseBody extends AbstractResponseBody {

  @SerializedName("edge_points")
  private List<EdgePointForUserEntity> edgePointList;

  public List<EdgePointForUserEntity> getEdgePointList() {
    return edgePointList;
  }

  public void setEdgePointList(List<EdgePointForUserEntity> edgePointList) {
    this.edgePointList = edgePointList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
