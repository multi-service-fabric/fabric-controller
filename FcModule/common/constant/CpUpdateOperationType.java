package msf.fc.common.constant;

public enum CpUpdateOperationType {

  STATIC_ROUTE_ADD("add"),
  STATIC_ROUTE_DELETE("delete");

  private String message;

  private CpUpdateOperationType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static CpUpdateOperationType getEnumFromMessage(String message) {
    for (CpUpdateOperationType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
