
package msf.mfcfc.services.priorityroutes.scenario.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;

public class NodeGroupPriorityRequest extends RestRequestBase {

  private String clusterId;

  public NodeGroupPriorityRequest(String requestBody, String notificationAddress, String notificationPort,
      String clusterId) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
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
