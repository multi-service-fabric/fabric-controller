
package msf.mfcfc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForOwnerEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class EdgePointReadOwnerResponseBody extends AbstractResponseBody {

  @SerializedName("edge_point")
  private EdgePointForOwnerEntity edgePoint;

  public EdgePointForOwnerEntity getEdgePoint() {
    return edgePoint;
  }

  public void setEdgePoint(EdgePointForOwnerEntity edgePoint) {
    this.edgePoint = edgePoint;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
