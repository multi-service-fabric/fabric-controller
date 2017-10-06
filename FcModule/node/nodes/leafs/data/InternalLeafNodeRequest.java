package msf.fc.node.nodes.leafs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.core.scenario.RestRequestBase;

public class InternalLeafNodeRequest extends RestRequestBase {

  private String nodeId;

  private String clusterId;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
