package msf.fc.common.constant;

public enum InterfaceOperationStatus {

  UNKNOWN(0, "unknown"),
  UP(1, "up"),
  DOWN(2, "down");

  private int code;

  private String message;

  private InterfaceOperationStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static InterfaceOperationStatus getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (InterfaceOperationStatus enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }
    return null;
  }

  public static InterfaceOperationStatus getEnumFromMessage(String message) {
    for (InterfaceOperationStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
