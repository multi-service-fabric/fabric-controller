package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class MsdpEcEntity {

  @SerializedName("peer")
  private PeerEcEntity peer;

  public PeerEcEntity getPeer() {
    return peer;
  }

  public void setPeer(PeerEcEntity peer) {
    this.peer = peer;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
