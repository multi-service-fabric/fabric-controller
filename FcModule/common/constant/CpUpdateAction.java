package msf.fc.common.constant;

public enum CpUpdateAction {

  ACTIVATE_RESERVE("activate_reserve"),
  DEACTIVATE_RESERVE("deactivate_reserve"),
  RESERVE_CANCEL("reserve_cancel"),
  UPDATE("update"),
  FORCE_DELETE("force_delete");

  private String message;

  private CpUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static CpUpdateAction getEnumFromMessage(String message) {
    for (CpUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
