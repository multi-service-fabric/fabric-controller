
package msf.mfcfc.services.renewal.scenario.renewals.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.services.renewal.common.constant.RenewalControllerType;

public class RenewalRequest extends RestRequestBase {

  private String controller;

  private String cluster;

  public RenewalRequest(String requestBody, String notificationAddress, String notificationPort, String controller,
      String cluster) {
    super(requestBody, notificationAddress, notificationPort);
    this.controller = controller;
    this.cluster = cluster;
  }

  public String getController() {
    return controller;
  }

  public void setController(String controller) {
    this.controller = controller;
  }

  public String getCluster() {
    return cluster;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }

  public RenewalControllerType getControllerEnum() {
    return RenewalControllerType.getEnumFromMessage(controller);
  }

  public void setControllerEnum(RenewalControllerType controller) {
    this.controller = controller.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
