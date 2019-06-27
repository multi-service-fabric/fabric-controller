
package msf.mfcfc.services.silentfailure.common.constant;

public enum DetectionTriggerType {

  PING("ping"),

  OSPFNBR("ospfNbr");

  private String message;

  private DetectionTriggerType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static DetectionTriggerType getEnumFromMessage(String message) {
    for (DetectionTriggerType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
