
package msf.mfcfc.services.ctrlstsnotify.common.constant;

public enum SystemType {

  OPERATING_SYSTEM("operating_system"),

  CTL_PROCESS("ctl_process");

  private String message;

  private SystemType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static SystemType getEnumFromMessage(String message) {
    for (SystemType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
