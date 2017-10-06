package msf.fc.common.constant;

public enum SliceType {
  L2_SLICE(2, "l2vpn"),
  L3_SLICE(3, "l3vpn");

  private int code;

  private String message;

  private SliceType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static SliceType getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (SliceType enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }
    return null;
  }

  public static SliceType getEnumFromMessage(String message) {
    for (SliceType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
