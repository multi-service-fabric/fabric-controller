
package msf.mfcfc.common.constant;

public enum RestUserTypeOption {

  OPERATOR("operator");

  private String message;

  private RestUserTypeOption(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static RestUserTypeOption getEnumFromMessage(String message) {
    for (RestUserTypeOption enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
