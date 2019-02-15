
package msf.mfcfc.common.constant;

public enum NodeOperationStatus {

  WAITING(0, "unexecuted"),

  RUNNING(100, "executing");

  private int code;

  private String message;

  private NodeOperationStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static NodeOperationStatus getEnumFromCode(int code) {
    for (NodeOperationStatus enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  public static NodeOperationStatus getEnumFromMessage(String message) {
    for (NodeOperationStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
