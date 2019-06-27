
package msf.mfcfc.services.silentfailure.common.constant;

public enum MonitoringResultType {

  SUCCEEDED("succeeded"),

  FAILED("failed");

  private String message;

  private MonitoringResultType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static MonitoringResultType getEnumFromMessage(String message) {
    for (MonitoringResultType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
