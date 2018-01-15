package msf.mfcfc.common.constant;


public enum EcVlanIfUpdateOperationType {

  
  ADD("add"),
  
  DELETE("delete");


  private String message;

  private EcVlanIfUpdateOperationType(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static EcVlanIfUpdateOperationType getEnumFromMessage(String message) {
    for (EcVlanIfUpdateOperationType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
