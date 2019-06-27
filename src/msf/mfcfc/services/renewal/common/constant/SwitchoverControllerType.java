
package msf.mfcfc.services.renewal.common.constant;

public enum SwitchoverControllerType {

  MFC("mfc"),

  FC("fc"),

  EC("ec"),

  EM("em");

  private String message;

  private SwitchoverControllerType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static SwitchoverControllerType getEnumFromMessage(String message) {
    if (message == null) {

    }
    for (SwitchoverControllerType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
