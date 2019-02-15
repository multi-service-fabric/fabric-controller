
package msf.mfcfc.common.constant;

public enum EquipmentVlanTrafficCapability {

  MIB("MIB", "MIB"),

  CLI("CLI", "CLI");

  private String code;

  private String message;

  private EquipmentVlanTrafficCapability(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static EquipmentVlanTrafficCapability getEnumFromCode(String code) {
    for (EquipmentVlanTrafficCapability enumValue : values()) {
      if (enumValue.getCode().equals(code)) {
        return enumValue;
      }
    }

    return null;
  }

  public static EquipmentVlanTrafficCapability getEnumFromMessage(String message) {
    for (EquipmentVlanTrafficCapability enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
