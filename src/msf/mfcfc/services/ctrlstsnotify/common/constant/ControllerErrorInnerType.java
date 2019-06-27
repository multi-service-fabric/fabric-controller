
package msf.mfcfc.services.ctrlstsnotify.common.constant;

public enum ControllerErrorInnerType {

  EC_ACT("ec_act"),

  EC_SBY("ec_sby"),

  EM_ACT("em_act"),

  EM_SBY("em_sby");

  private String message;

  private ControllerErrorInnerType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static ControllerErrorInnerType getEnumFromMessage(String message) {
    for (ControllerErrorInnerType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
