package msf.mfcfc.common.constant;


public enum ErrorResponseDataConsistency {

  
  ROLLED_BACK("rolled back"),
  
  UPDATED("updated"),
  
  NOT_CONSISTENT("not consistent"),
  
  UNKNOWN("unknown");


  private String message;

  private ErrorResponseDataConsistency(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static ErrorResponseDataConsistency getEnumFromMessage(String message) {
    for (ErrorResponseDataConsistency enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
