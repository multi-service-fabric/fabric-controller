
package msf.mfcfc.common.constant;

public enum EcBlockadeStatus {

  INSERVICE("inservice"),

  BUSY("busy");

  private String message;

  private EcBlockadeStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcBlockadeStatus getEnumFromMessage(String message) {
    for (EcBlockadeStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
