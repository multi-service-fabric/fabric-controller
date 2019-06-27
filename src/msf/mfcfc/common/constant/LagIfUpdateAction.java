
package msf.mfcfc.common.constant;

public enum LagIfUpdateAction {

  ADD_IF("add_if"),

  DEL_IF("del_if");

  private String message;

  private LagIfUpdateAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static LagIfUpdateAction getEnumFromMessage(String message) {
    for (LagIfUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
