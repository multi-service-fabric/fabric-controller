
package msf.mfcfc.services.silentfailure.common.constant;

public enum BlockadeResultType {

  COMPLETED("completed"),

  FAILED("failed"),

  NONE("none");

  private String message;

  private BlockadeResultType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static BlockadeResultType getEnumFromMessage(String message) {
    for (BlockadeResultType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
