
package msf.mfcfc.common.constant;

public enum BlockadeStatusType {

  DOWN("down"),

  UP("up");

  private String message;

  private BlockadeStatusType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static BlockadeStatusType getEnumFromMessage(String message) {
    for (BlockadeStatusType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
