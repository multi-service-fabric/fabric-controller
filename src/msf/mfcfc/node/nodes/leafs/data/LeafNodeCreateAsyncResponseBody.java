
package msf.mfcfc.node.nodes.leafs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class LeafNodeCreateAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("node_id")
  private String nodeId;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
