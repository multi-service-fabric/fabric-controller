
package msf.mfcfc.services.silentfailure.common.constant;

public enum EcPingBetweenDevicesResult {

  SUCCESS("success"),

  FAILED("failed"),

  UNEXECUTED("unexecuted");

  private String message;

  private EcPingBetweenDevicesResult(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcPingBetweenDevicesResult getEnumFromMessage(String message) {
    for (EcPingBetweenDevicesResult enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
