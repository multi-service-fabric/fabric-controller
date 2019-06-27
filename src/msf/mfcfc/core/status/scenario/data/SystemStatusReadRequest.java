
package msf.mfcfc.core.status.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.core.scenario.RestRequestBase;

public class SystemStatusReadRequest extends RestRequestBase {

  private String controller;

  private String cluster;

  private String getInfo;

  public SystemStatusReadRequest(String requestBody, String notificationAddress, String notificationPort,
      String controller, String cluster, String getInfo) {
    super(requestBody, notificationAddress, notificationPort);
    this.controller = controller;
    this.cluster = cluster;
    this.getInfo = getInfo;
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

  public String getGetInfo() {
    return getInfo;
  }

  public void setGetInfo(String getInfo) {
    this.getInfo = getInfo;
  }

  public ControllerType getControllerEnum() {
    return ControllerType.getEnumFromMessage(controller);
  }

  public void setControllerEnum(ControllerType controller) {
    this.controller = controller.getMessage();
  }

  public GetInfo getGetInfoEnum() {
    return GetInfo.getEnumFromMessage(getInfo);
  }

  public void setGetInfoEnum(GetInfo getInfo) {
    this.getInfo = getInfo.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
