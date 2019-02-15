
package msf.mfcfc.services.filter.common.constant;

public enum FilterDirection {

  IN("in");

  private String message;

  private FilterDirection(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static FilterDirection getEnumFromDirection(String message) {
    for (FilterDirection enumValues : values()) {
      if (enumValues.getMessage().equals(message)) {
        return enumValues;
      }
    }
    return null;
  }
}
