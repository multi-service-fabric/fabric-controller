package msf.fc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalLinkLeafEntity {

  @SerializedName("local_if_id")
  private String localIfId;

  @SerializedName("speed")
  private String speed;

  @SerializedName("remote_spine_node_id")
  private String remoteSpineNodeId;

  @SerializedName("remote_if_id")
  private String remoteIfId;

  public String getLocalIfId() {
    return localIfId;
  }

  public void setLocalIfId(String localIfId) {
    this.localIfId = localIfId;
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public String getRemoteSpineNodeId() {
    return remoteSpineNodeId;
  }

  public void setRemoteSpineNodeId(String remoteSpineNodeId) {
    this.remoteSpineNodeId = remoteSpineNodeId;
  }

  public String getRemoteIfId() {
    return remoteIfId;
  }

  public void setRemoteIfId(String remoteIfId) {
    this.remoteIfId = remoteIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
