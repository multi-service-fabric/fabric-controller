
package msf.mfcfc.common.constant;

public enum VpnType {

  L2VPN("l2", "l2"),

  L3VPN("l3", "l3");

  private String code;

  private String message;

  private VpnType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static VpnType getEnumFromCode(String code) {
    for (VpnType enumValue : values()) {
      if (enumValue.getCode().equals(code)) {
        return enumValue;
      }
    }

    return null;
  }

  public static VpnType getEnumFromMessage(String message) {
    for (VpnType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
