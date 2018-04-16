
package msf.mfcfc.common.constant;

public enum SliceUnitReachableOppositeType {

  CP("cp"),

  CLUSTER_LINK_IF("cluster_link-if");

  private String message;

  private SliceUnitReachableOppositeType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static SliceUnitReachableOppositeType getEnumFromMessage(String message) {
    for (SliceUnitReachableOppositeType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
