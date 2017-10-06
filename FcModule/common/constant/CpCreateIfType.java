package msf.fc.common.constant;

public enum CpCreateIfType {

  LAG_IF("lag"),
  PHYSICAL_IF("physical");

  private String message;

  private CpCreateIfType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static CpCreateIfType getEnumFromMessage(String message) {
    for (CpCreateIfType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }
    return null;
  }

}
