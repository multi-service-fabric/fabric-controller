package msf.fc.common.constant;

public enum ProvisioningStatus {

  STOPPED(0, "stopped"),
  UNSET(1, "unset"),
  SETTING(2, "setting"),
  BOOT_FAILED(3, "boot_failed"),
  BOOT_COMPLETE(4, "boot_complete");

  private int code;

  private String message;

  private ProvisioningStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static ProvisioningStatus getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (ProvisioningStatus enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }
    return null;
  }

  public static ProvisioningStatus getEnumFromMessage(String message) {
    for (ProvisioningStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
