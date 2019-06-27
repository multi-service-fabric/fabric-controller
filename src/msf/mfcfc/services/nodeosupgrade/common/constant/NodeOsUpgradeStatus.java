
package msf.mfcfc.services.nodeosupgrade.common.constant;

public enum NodeOsUpgradeStatus {

  WAITING(0, "unexecuted"),

  DETOURED(100, "detoured"),

  OS_UPGRADING(110, "os_upgrading"),

  RECOVERING(120, "recovering"),

  RECOVERED(130, "recovered"),

  COMPLETED(200, "completed"),

  OPERATOR_CHECKING(210, "operator_checking"),

  FAILED_NOT_OS_UPGRADED_NOT_DETOURED(1, "failed_not_os_upgraded_not_detoured"),

  FAILED_NOT_OS_UPGRADED_DETOURED(101, "failed_not_os_upgraded_detoured"),

  FAILED_NOT_RECOVERED(121, "failed_not_recovered"),

  FAILED_RECOVERED_DETOURED(131, "failed_recovered_detoured"),

  FAILED_UNKNOWN_DETOURED(901, "failed_unknown_detoured");

  private int code;

  private String message;

  private NodeOsUpgradeStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static NodeOsUpgradeStatus getEnumFromCode(int code) {
    for (NodeOsUpgradeStatus enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  public static NodeOsUpgradeStatus getEnumFromMessage(String message) {
    for (NodeOsUpgradeStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
