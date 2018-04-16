
package msf.mfcfc.common.constant;

public enum ErrorResponseRequestResult {

  SUCCESS("success"),

  FAILED("failed");

  private String message;

  private ErrorResponseRequestResult(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static ErrorResponseRequestResult getEnumFromMessage(String message) {
    for (ErrorResponseRequestResult enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
