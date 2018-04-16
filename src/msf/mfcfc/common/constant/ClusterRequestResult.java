
package msf.mfcfc.common.constant;

public enum ClusterRequestResult {

  SUCCESS("success"),

  FAILED("failed");

  private String message;

  private ClusterRequestResult(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static ClusterRequestResult getEnumFromMessage(String message) {
    for (ClusterRequestResult enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
