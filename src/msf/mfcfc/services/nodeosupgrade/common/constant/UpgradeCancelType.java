
package msf.mfcfc.services.nodeosupgrade.common.constant;

public enum UpgradeCancelType {

  CANCEL("cancel");

  private String message;

  private UpgradeCancelType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static UpgradeCancelType getEnumFromMessage(String message) {
    for (UpgradeCancelType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
