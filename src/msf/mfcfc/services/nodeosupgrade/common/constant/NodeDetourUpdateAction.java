
package msf.mfcfc.services.nodeosupgrade.common.constant;

public enum NodeDetourUpdateAction {

  UPDATE("update");

  private String message;

  private NodeDetourUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static NodeDetourUpdateAction getEnumFromMessage(String message) {
    for (NodeDetourUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
