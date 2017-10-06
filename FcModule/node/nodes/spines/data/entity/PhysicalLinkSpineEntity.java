package msf.fc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalLinkSpineEntity {

  @SerializedName("local_if_id")
  private String localIfId;

  @SerializedName("speed")
  private String speed;

  @SerializedName("remote_leaf_node_id")
  private String remoteLeafNodeId;

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

  public String getRemoteLeafNodeId() {
    return remoteLeafNodeId;
  }

  public void setRemoteLeafNodeId(String remoteSpineNodeId) {
    this.remoteLeafNodeId = remoteSpineNodeId;
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
