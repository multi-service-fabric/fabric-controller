
package msf.mfcfc.common.constant;

public enum EcLagLinkType {

  INTERNAL_LINK("internal_link"),

  OTHER("other");

  private String message;

  private EcLagLinkType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public EcLagLinkType getEnumFromMessage(String message) {
    for (EcLagLinkType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;

  }
}
