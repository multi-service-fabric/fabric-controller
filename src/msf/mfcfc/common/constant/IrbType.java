
package msf.mfcfc.common.constant;

public enum IrbType {

  ASYMMETRIC(1, "asymmetric"),

  SYMMETRIC(2, "symmetric"),

  NONE(-1, "none");

  private int code;

  private String message;

  private IrbType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static IrbType getEnumFromCode(Integer code) {
    if (code == null) {
      return NONE;
    }
    for (IrbType enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  public static IrbType getEnumFromMessage(String message) {
    if (message == null) {
      return IrbType.NONE;
    }
    for (IrbType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

  public static IrbType getEnumFromEcMessage(String ecMessage) {
    return getEnumFromMessage(ecMessage);
  }
}
