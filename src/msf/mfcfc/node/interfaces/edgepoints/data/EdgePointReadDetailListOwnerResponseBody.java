package msf.mfcfc.node.interfaces.edgepoints.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.edgepoints.data.entity.EdgePointForOwnerEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class EdgePointReadDetailListOwnerResponseBody extends AbstractResponseBody {
  
  @SerializedName("edge_points")
  private List<EdgePointForOwnerEntity> edgePointList;

  
  public List<EdgePointForOwnerEntity> getEdgePointList() {
    return edgePointList;
  }

  
  public void setEdgePointList(List<EdgePointForOwnerEntity> edgePointList) {
    this.edgePointList = edgePointList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
