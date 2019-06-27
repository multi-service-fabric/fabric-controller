
package msf.mfcfc.services.nodeosupgrade.common.constant;

public enum NodeOsUpgradeOperationType {

  OS_UPGRADE("os_upgrade"),

  RECOVER_NODE("recover_node");

  private String message;

  private NodeOsUpgradeOperationType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static NodeOsUpgradeOperationType getEnumFromMessage(String message) {
    for (NodeOsUpgradeOperationType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
