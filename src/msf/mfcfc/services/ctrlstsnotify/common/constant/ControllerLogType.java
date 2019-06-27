
package msf.mfcfc.services.ctrlstsnotify.common.constant;

public enum ControllerLogType {

  FC_ACT("fc_act"),

  EC_ACT("ec_act"),

  EM_ACT("em_act");

  private String message;

  private ControllerLogType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static ControllerLogType getEnumFromMessage(String message) {
    for (ControllerLogType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
