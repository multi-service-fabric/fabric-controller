
package msf.mfcfc.common.constant;

public enum InterfaceType {

  LAG_IF("lag-if", "lag-ifs"),

  PHYSICAL_IF("physical-if", "physical-ifs"),

  BREAKOUT_IF("breakout-if", "breakout-ifs"),

  VLAN_IF("vlan-if", "vlan-ifs");

  private String message;

  private String pluralMessage;

  private InterfaceType(String message, String pluralMessage) {
    this.message = message;
    this.pluralMessage = pluralMessage;
  }

  public String getMessage() {
    return message;
  }

  public String getPluralMessage() {
    return pluralMessage;
  }

  public static InterfaceType getEnumFromMessage(String message) {
    for (InterfaceType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

  public static InterfaceType getEnumFromPluralMessage(String pluralMessage) {
    for (InterfaceType enumValue : values()) {
      if (enumValue.getPluralMessage().equals(pluralMessage)) {
        return enumValue;
      }
    }

    return null;
  }
}
