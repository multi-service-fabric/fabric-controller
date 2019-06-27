
package msf.mfcfc.common.constant;

public enum SpineNodeUpdateAction {

  CHG_EQUIPMENT_TYPE("chg_equipment_type");

  private String message;

  private SpineNodeUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static SpineNodeUpdateAction getEnumFromMessage(String message) {
    for (SpineNodeUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
