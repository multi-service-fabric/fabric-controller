
package msf.mfcfc.node.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;

public class InternalNodeRequest extends RestRequestBase {

  public InternalNodeRequest(String requestBody, String notificationAddress, String notificationPort, String nodeId) {
    super(requestBody, notificationAddress, notificationPort);
    this.nodeId = nodeId;
  }

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
