
package msf.mfcfc.services.filter.common.constant;

public enum EcFilterUpdateOperation {

  ADD("add"),

  DELETE("delete");

  private String message;

  private EcFilterUpdateOperation(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcFilterUpdateOperation getEnumFromMessage(String message) {
    for (EcFilterUpdateOperation enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
