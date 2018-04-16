
package msf.mfcfc.common.constant;

public enum NodeSubStatus {

  ZTP_FEASIBLE("ztp-feasible"),

  ZTP_INFEASIBLE("ztp-infeasible");

  private String message;

  private NodeSubStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static NodeSubStatus getEnumFromMessage(String message) {
    for (NodeSubStatus enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
