package msf.fc.common.constant;

public enum ReserveStatus {

  NONE(0, "none"),
  ACTIVATE_RESERVE(1, "activate_reserve"),
  DEACTIVATE_RESERVE(2, "deactivate_reserve");

  private int code;

  private String message;

  private ReserveStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static ReserveStatus getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (ReserveStatus enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }
    return null;
  }

  public static ReserveStatus getEnumFromMessage(String message) {
    for (ReserveStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
