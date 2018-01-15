package msf.mfcfc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class EdgePointCreateResponseBody extends AbstractResponseBody {

  
  @SerializedName("edge_point_id")
  private String edgePointId;

  
  public String getEdgePointId() {
    return edgePointId;
  }

  
  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
