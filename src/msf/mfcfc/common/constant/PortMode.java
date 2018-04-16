
package msf.mfcfc.common.constant;

public enum PortMode {

  ACCESS(1, "access"),

  TRUNK(2, "trunk");

  private int code;

  private String message;

  private PortMode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static PortMode getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (PortMode enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }

    return null;
  }

  public static PortMode getEnumFromMessage(String message) {
    for (PortMode enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
