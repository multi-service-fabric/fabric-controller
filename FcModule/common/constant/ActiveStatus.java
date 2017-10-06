package msf.fc.common.constant;

public enum ActiveStatus {

  INACTIVE(0, "inactive"),
  ACTIVE(1, "active");

  private int code;

  private String message;

  private ActiveStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static ActiveStatus getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (ActiveStatus enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }
    return null;
  }

  public static ActiveStatus getEnumFromMessage(String message) {
    for (ActiveStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
