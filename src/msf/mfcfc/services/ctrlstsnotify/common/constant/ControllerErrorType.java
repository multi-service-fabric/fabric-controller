
package msf.mfcfc.services.ctrlstsnotify.common.constant;

public enum ControllerErrorType {

  FC_ACT("fc_act"),

  FC_SBY("fc_sby"),

  EC_ACT("ec_act"),

  EC_SBY("ec_sby"),

  EM_ACT("em_act"),

  EM_SBY("em_sby");

  private String message;

  private ControllerErrorType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static ControllerErrorType getEnumFromMessage(String message) {
    for (ControllerErrorType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
