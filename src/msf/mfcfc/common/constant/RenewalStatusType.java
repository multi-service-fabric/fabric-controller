
package msf.mfcfc.common.constant;

public enum RenewalStatusType {

  NONE(0, "none"),

  RENEWAL_IN_PROGRESS(1, "renewal_in_progress"),

  UNKNOWN(-1, "unknown");

  private int code;

  private String message;

  private RenewalStatusType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static RenewalStatusType getEnumFromCode(int code) {
    for (RenewalStatusType enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return RenewalStatusType.UNKNOWN;
  }

  public static RenewalStatusType getEnumFromMessage(String message) {
    if (message == null) {
      return RenewalStatusType.UNKNOWN;
    }
    for (RenewalStatusType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
