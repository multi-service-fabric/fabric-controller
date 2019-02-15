
package msf.mfcfc.services.filter.common.constant;

public enum EcFilterIfType {

  PHYSICAL_IFS("physical-ifs"),

  LAG_IFS("lag-ifs"),

  VLAN_IFS("vlan-ifs");

  private String message;

  private EcFilterIfType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcFilterIfType getEnumFromMessage(String message) {
    for (EcFilterIfType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
