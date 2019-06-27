
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;

public class NodeOsUpgradeRequest extends RestRequestBase {

  private String operationId;

  public NodeOsUpgradeRequest(String requestBody, String notificationAddress, String notificationPort,
      String operationId) {
    super(requestBody, notificationAddress, notificationPort);
    this.operationId = operationId;
  }

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
