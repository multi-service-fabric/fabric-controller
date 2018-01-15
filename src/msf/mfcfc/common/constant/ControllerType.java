package msf.mfcfc.common.constant;


public enum ControllerType {

  
  MFC("mfc"),
  
  FC("fc"),
  
  EC("ec"),
  
  EM("em");


  private String message;

  private ControllerType(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static ControllerType getEnumFromMessage(String message) {
    for (ControllerType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
