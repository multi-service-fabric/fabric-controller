
package msf.mfcfc.common.constant;

public enum LogType {

  API_ACCESS("api_access"),

  PROCESSING("processing");

  private String message;

  private LogType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static LogType getEnumFromMessage(String message) {
    for (LogType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
