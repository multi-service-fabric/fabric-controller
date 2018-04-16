
package msf.mfcfc.common.constant;

public enum AddressType {

  IPV4(4, "ipv4"),

  IPV6(6, "ipv6");

  private int code;

  private String message;

  private AddressType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static AddressType getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (AddressType enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }

    return null;
  }

  public static AddressType getEnumFromMessage(String message) {
    for (AddressType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
