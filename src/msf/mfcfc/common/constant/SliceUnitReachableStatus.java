
package msf.mfcfc.common.constant;

public enum SliceUnitReachableStatus {

  REACHABLE("reachable"),

  UNREACHABLE("unreachable");

  private String message;

  private SliceUnitReachableStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static SliceUnitReachableStatus getEnumFromMessage(String message) {
    for (SliceUnitReachableStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
