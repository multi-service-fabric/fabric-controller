
package msf.mfcfc.services.filter.common.constant;

public enum FilterAction {

  DISCARD("discard"),;

  private String message;

  private FilterAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static FilterAction getEnumFromMessage(String message) {
    for (FilterAction enumValues : values()) {
      if (enumValues.getMessage().equals(message)) {
        return enumValues;
      }
    }
    return null;
  }

}
