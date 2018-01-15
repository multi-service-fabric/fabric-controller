package msf.mfcfc.common.constant;


public enum ControllerEvent {

  
  START_SYSTEM_SWITCHING("start system switching"),
  
  END_SYSTEM_SWITCHING("end system switching"),
  
  START_BLOCKADE("start blockade"),
  
  END_BLOCKADE("end blockade");


  private String message;

  private ControllerEvent(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static ControllerEvent getEnumFromMessage(String message) {
    for (ControllerEvent enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
