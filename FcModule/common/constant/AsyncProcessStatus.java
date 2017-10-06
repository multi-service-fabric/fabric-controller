package msf.fc.common.constant;

public enum AsyncProcessStatus {

  WAITING(0, "unexecuted"),

  RUNNING(100, "executing"),

  COMPLETED(200, "completed"),

  FAILED(400, "failed"),

  CANCELED(500, "canceled");

  private int code;

  private String message;

  private AsyncProcessStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static AsyncProcessStatus getEnumFromCode(int code) {
    for (AsyncProcessStatus enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }
    return null;
  }

  public static AsyncProcessStatus getEnumFromMessage(String message) {
    for (AsyncProcessStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }
}