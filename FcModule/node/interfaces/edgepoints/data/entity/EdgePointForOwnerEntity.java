package msf.fc.node.interfaces.edgepoints.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EdgePointForOwnerEntity {

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("base_if")
  private BaseIfEntity baseIf;

  @SerializedName("support_protocols")
  private SupportProtocolEntity supportProtocol;

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

  public SupportProtocolEntity getSupportProtocol() {
    return supportProtocol;
  }

  public void setSupportProtocol(SupportProtocolEntity supportProtocol) {
    this.supportProtocol = supportProtocol;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
