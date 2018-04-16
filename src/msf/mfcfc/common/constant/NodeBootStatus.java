
package msf.mfcfc.common.constant;

public enum NodeBootStatus {

  SUCCESS("success"),

  FAILED("failed"),

  CANCEL("cancel");

  private String message;

  private NodeBootStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static NodeBootStatus getEnumFromMessage(String message) {
    for (NodeBootStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
