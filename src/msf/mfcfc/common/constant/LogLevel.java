package msf.mfcfc.common.constant;


public enum LogLevel {

  
  ERROR("error"),

  
  WARNING("warning"),

  
  INFO("info");


  private String message;

  private LogLevel(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static LogLevel getEnumFromMessage(String message) {
    for (LogLevel enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
