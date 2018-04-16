
package msf.mfcfc.common.constant;

public enum SliceUpdateAction {

  UPDATE_REMARK_MENU("update_remark_menu");

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
