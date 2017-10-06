package msf.fc.node.interfaces.edgepoints.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2EdgePointForOwnerEntity {

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("base_if")
  private BaseIfEntity baseIf;

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  public BaseIfEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(BaseIfEntity baseIf) {
    this.baseIf = baseIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
