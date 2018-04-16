
package msf.mfcfc.common.constant;

public enum EquipmentRouterType {

  NORMAL_ROUTER("normal"),

  CORE_ROUTER("core-router");

  private String message;

  private EquipmentRouterType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EquipmentRouterType getEnumFromMessage(String message) {
    for (EquipmentRouterType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
