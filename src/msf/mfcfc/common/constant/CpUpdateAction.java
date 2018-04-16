
package msf.mfcfc.common.constant;

public enum CpUpdateAction {

  UPDATE("update");

  private String message;

  private CpUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static CpUpdateAction getEnumFromMessage(String message) {
    for (CpUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
