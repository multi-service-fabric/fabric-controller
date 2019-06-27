
package msf.mfcfc.services.ctrlstsnotify.common.constant;

public enum ControllerLogInnerType {

  EC_ACT("ec_act"),

  EM_ACT("em_act");

  private String message;

  private ControllerLogInnerType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static ControllerLogInnerType getEnumFromMessage(String message) {
    for (ControllerLogInnerType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
