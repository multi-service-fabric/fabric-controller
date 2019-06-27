
package msf.mfcfc.common.constant;

public enum EcLagIfUpdateAction {

  ADD("add", LagIfUpdateAction.ADD_IF),

  DELETE("delete", LagIfUpdateAction.DEL_IF);

  private String message;
  private LagIfUpdateAction actionEnum;

  private EcLagIfUpdateAction(String message, LagIfUpdateAction actionEnum) {
    this.message = message;
    this.actionEnum = actionEnum;
  }

  public String getMessage() {
    return message;
  }

  public LagIfUpdateAction getActionEnum() {
    return actionEnum;
  }

  public static EcLagIfUpdateAction getEnumFromMessage(String message) {
    for (EcLagIfUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

  public static EcLagIfUpdateAction getEnumFromLagIfUpdateAction(LagIfUpdateAction action) {
    for (EcLagIfUpdateAction enumValue : values()) {
      if (enumValue.getActionEnum().equals(action)) {
        return enumValue;
      }
    }

    return null;
  }
}
