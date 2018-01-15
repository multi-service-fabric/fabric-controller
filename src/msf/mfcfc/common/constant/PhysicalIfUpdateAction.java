package msf.mfcfc.common.constant;


public enum PhysicalIfUpdateAction {

  
  SPEED_SET("speed_set"),
  
  SPEED_DELETE("speed_delete");


  private String message;

  private PhysicalIfUpdateAction(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static PhysicalIfUpdateAction getEnumFromMessage(String message) {
    for (PhysicalIfUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
