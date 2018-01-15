package msf.mfcfc.common.constant;


public enum InterfaceOperationStatus {
  
  UP("up"),
  
  DOWN("down"),
  
  UNKNOWN("unknown");


  private String message;

  private InterfaceOperationStatus(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
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
