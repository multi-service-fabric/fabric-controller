
package msf.mfcfc.common.constant;

public enum NodeStatus {

  IN_SERVICE("in-service"),

  BEFORE_SETTING("before-setting"),

  ZTP_COMPLETE("ztp-complete"),

  NODE_RESETTING_COMPLETE("node-resetting-complete"),

  FAILURE_SETTING("failure-setting"),

  FAILURE_NODE_RESETTING("failure-node-resetting"),

  FAILURE_SERVICE_SETTING("failure-service-setting"),

  FAILURE_OTHER("failure-other"),

  FAILURE_RECOVER_NODE("failure-recover-node");

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
