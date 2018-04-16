
package msf.mfcfc.common.constant;

public enum GetInfo {

  OS_CPU("os-cpu"),

  OS_MEM("os-mem"),

  OS_DISK("os-disk"),

  OS_TRAFFIC("os-traffic"),

  CTR_CPU("ctr-cpu"),

  CTR_MEM("ctr-mem"),

  CTR_STATE("ctr-state"),

  CTR_RECEIVE_REQ("ctr-receive_req"),

  CTR_SEND_REQ("ctr-send_req");

  private String message;

  private GetInfo(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static GetInfo getEnumFromMessage(String message) {
    for (GetInfo enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
