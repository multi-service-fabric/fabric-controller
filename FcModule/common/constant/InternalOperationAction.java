package msf.fc.common.constant;

public enum InternalOperationAction {

  GET_LOGICAL_IF_STATUS("get_logical_if_status"),
  UPDATE_LOGICAL_IF_STATUS("update_logical_if_status");

  private String message;

  private InternalOperationAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static InternalOperationAction getEnumFromMessage(String message) {
    for (InternalOperationAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
