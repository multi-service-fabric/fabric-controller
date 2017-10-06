package msf.fc.common.constant;

public enum RoleType {

  MASTER(1, "master"),
  SLAVE(2, "slave");

  private int code;

  private String message;

  private RoleType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static RoleType getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (RoleType enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }
    return null;
  }

  public static RoleType getEnumFromMessage(String message) {
    for (RoleType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
