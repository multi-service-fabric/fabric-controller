package msf.fc.node.interfaces.edgepoints.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EdgePointForUserEntity {

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("support_protocols")
  private SupportProtocolEntity supportProtocol;

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
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
