
package msf.mfcfc.node.nodes.rrs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class RrNodeRrEntity extends AbstractResponseBody {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("router_id")
  private String routerId;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getRouterId() {
    return routerId;
  }

  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
