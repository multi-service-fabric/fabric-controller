
package msf.mfcfc.common.constant;

public enum ServiceStatus {

  STOPPED(0, "stopped"),

  INITIALIZING(10, "start-up in progress"),

  SWITCHING(50, "system switching"),

  FINALIZING(90, "shutdown in progress"),

  STARTED(100, "running"),

  UNKNOWN(-1, "unknown");

  private int code;

  private String message;

  private ServiceStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static ServiceStatus getEnumFromCode(int code) {
    for (ServiceStatus enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  public static ServiceStatus getEnumFromMessage(String message) {
    for (ServiceStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
