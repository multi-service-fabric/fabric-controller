
package msf.mfcfc.services.renewal.common.constant;

public enum RenewalSetAction {

  START("start"),

  STOP("stop");

  private String message;

  private RenewalSetAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static RenewalSetAction getEnumFromMessage(String message) {
    for (RenewalSetAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
