
package msf.mfcfc.common.constant;

public enum FailureStatus {

  UP("up"),

  DOWN("down"),

  WARN("warn");

  private String message;

  private FailureStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static FailureStatus getEnumFromMessage(String message) {
    for (FailureStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
