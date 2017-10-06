package msf.fc.common.constant;

public enum EcCommonOperationAction {

  CREATE_L2CPS("create_l2cps"),
  DELETE_L2CPS("delete_l2cps"),
  CREATE_L3CPS("create_l3cps"),
  DELETE_L3CPS("delete_l3cps");

  private String message;

  private EcCommonOperationAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcCommonOperationAction getEnumFromMessage(String message) {
    for (EcCommonOperationAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
