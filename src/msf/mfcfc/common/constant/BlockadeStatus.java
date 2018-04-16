
package msf.mfcfc.common.constant;

public enum BlockadeStatus {

  NONE(0, "none"),

  BLOCKADE(1, "blockade"),

  WARNING(-1, "warning"),

  UNKNOWN(-2, "unknown");

  private int code;

  private String message;

  private BlockadeStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static BlockadeStatus getEnumFromCode(int code) {
    for (BlockadeStatus enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  public static BlockadeStatus getEnumFromMessage(String message) {
    for (BlockadeStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
