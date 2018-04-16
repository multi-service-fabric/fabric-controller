
package msf.mfcfc.common.constant;

public enum EcEmServiceStatus {

  INSERVICE("inservice"),

  STARTREADY("startready"),

  STOPREADY("stopready"),

  CHANGEOVER("changeover"),

  UNKNOWN("unknown");

  private String message;

  private EcEmServiceStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcEmServiceStatus getEnumFromMessage(String message) {
    for (EcEmServiceStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
