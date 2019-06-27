
package msf.mfcfc.services.silentfailure.common.constant;

public enum EcOspfNeighborStatus {

  UP("up"),

  DOWN("down"),

  NOTFOUND("notfound"),

  UNEXECUTED("unexecuted");

  private String message;

  private EcOspfNeighborStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcOspfNeighborStatus getEnumFromMessage(String message) {
    for (EcOspfNeighborStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
