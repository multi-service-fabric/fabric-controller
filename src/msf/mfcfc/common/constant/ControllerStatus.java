package msf.mfcfc.common.constant;


public enum ControllerStatus {

  
  WARNING(0, "warning"),

  
  RUNNING(100, "running");


  private int code;


  private String message;

  private ControllerStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  
  public int getCode() {
    return code;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static ControllerStatus getEnumFromCode(int code) {
    for (ControllerStatus enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static ControllerStatus getEnumFromMessage(String message) {
    for (ControllerStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
