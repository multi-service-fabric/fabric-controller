package msf.fc.common.constant;

public enum SliceUpdateAction {

  DEACTIVATE("deactivate"),
  ACTIVATE("activate"),
  ACTIVATE_RESERVE("activate_reserve"),
  DEACTIVATE_RESERVE("deactivate_reserve"),
  RESERVE_CANCEL("reserve_cancel"),
  ACTIVATE_CPS("activate_cps"),
  DEACTIVATE_CPS("deactivate_cps");

  private String message;

  private SliceUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static SliceUpdateAction getEnumFromMessage(String message) {
    for (SliceUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
