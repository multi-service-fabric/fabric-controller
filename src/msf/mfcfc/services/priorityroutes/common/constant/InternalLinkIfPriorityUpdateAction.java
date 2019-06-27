
package msf.mfcfc.services.priorityroutes.common.constant;

public enum InternalLinkIfPriorityUpdateAction {

  UPDATE("update");

  private String message;

  private InternalLinkIfPriorityUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static InternalLinkIfPriorityUpdateAction getEnumFromMessage(String message) {
    for (InternalLinkIfPriorityUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
