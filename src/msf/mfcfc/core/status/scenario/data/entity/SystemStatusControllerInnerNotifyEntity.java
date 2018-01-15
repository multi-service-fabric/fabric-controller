package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ControllerEvent;
import msf.mfcfc.common.constant.ControllerType;


public class SystemStatusControllerInnerNotifyEntity {

  
  @SerializedName("controller_type")
  private String controllerType;

  
  @SerializedName("event")
  private String event;

  
  public String getControllerType() {
    return controllerType;
  }

  
  public void setControllerType(String controllerType) {
    this.controllerType = controllerType;
  }

  
  public String getEvent() {
    return event;
  }

  
  public void setEvent(String event) {
    this.event = event;
  }

  
  public ControllerType getControllerTypeEnum() {
    return ControllerType.getEnumFromMessage(controllerType);
  }

  
  public void setControllerTypeEnum(ControllerType controllerType) {
    this.controllerType = controllerType.getMessage();
  }

  
  public ControllerEvent getControllerEventEnum() {
    return ControllerEvent.getEnumFromMessage(event);
  }

  
  public void setControllerEventEnum(ControllerEvent event) {
    this.event = event.getMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
