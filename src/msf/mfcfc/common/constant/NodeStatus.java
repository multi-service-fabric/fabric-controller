package msf.mfcfc.common.constant;


public enum NodeStatus {

  
  IN_SERVICE("in-service"),
  
  BEFORE_SETTING("before-setting"),
  
  ZTP_COMPLETE("ztp-complete"),
  
  NODE_RESETTING_COMPLETE("node-resetting-complete"),
  
  FAILER_SETTING("failer-setting"),
  
  FAILER_NODE_RESETTING("failer-node-resetting"),
  
  FAILER_SERVICE_SETTING("failer-service-setting"),
  
  FAILER_OTHER("failer-other");


  private String message;

  private NodeStatus(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static NodeStatus getEnumFromMessage(String message) {
    for (NodeStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
