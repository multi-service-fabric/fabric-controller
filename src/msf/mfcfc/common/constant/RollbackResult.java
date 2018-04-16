
package msf.mfcfc.common.constant;

public enum RollbackResult {

  SUCCESS("completed"),

  FAILED("failed"),

  NONE("none");

  private String message;

  private RollbackResult(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static RollbackResult getEnumFromMessage(String message) {
    for (RollbackResult enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
