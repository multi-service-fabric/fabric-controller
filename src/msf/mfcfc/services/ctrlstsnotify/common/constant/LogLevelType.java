
package msf.mfcfc.services.ctrlstsnotify.common.constant;

public enum LogLevelType {

  ERROR("ERROR"),

  WARNING("WARNING"),

  INFO("INFO");

  private String message;

  private LogLevelType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static LogLevelType getEnumFromMessage(String message) {
    for (LogLevelType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
