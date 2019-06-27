
package msf.mfcfc.services.nodeosupgrade.common.constant;

public enum OsUpgradeResultType {

  SUCCEEDED("succeeded"),

  FAILED("failed");

  private String message;

  private OsUpgradeResultType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static OsUpgradeResultType getEnumFromMessage(String message) {
    for (OsUpgradeResultType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
