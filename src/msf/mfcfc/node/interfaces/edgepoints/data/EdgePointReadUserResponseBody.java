
package msf.mfcfc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForUserEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class EdgePointReadUserResponseBody extends AbstractResponseBody {

  @SerializedName("edge_point")
  private EdgePointForUserEntity edgePoint;

  public EdgePointForUserEntity getEdgePoint() {
    return edgePoint;
  }

  public void setEdgePoint(EdgePointForUserEntity edgePoint) {
    this.edgePoint = edgePoint;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
