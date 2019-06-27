
package msf.mfcfc.services.renewal.common.constant;

public enum RenewalControllerType {

  MFC("mfc"),

  FC("fc");

  private String message;

  private RenewalControllerType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static RenewalControllerType getEnumFromMessage(String message) {
    if (message == null) {

    }
    for (RenewalControllerType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
