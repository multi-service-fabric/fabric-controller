
package msf.mfcfc.common.constant;

public enum EcCommonOperationAction {

  CREATE_UPDATE_L2VLAN_IF("create_update_l2vlan_if"),

  DELETE_UPDATE_L2VLAN_IF("delete_update_l2vlan_if"),

  CREATE_L3VLAN_IF("create_l3vlan_if"),

  DELETE_L3VLAN_IF("delete_l3vlan_if"),

  UPDATE_L2VLAN_IF("update_l2vlan_if"),

  UPDATE_L3VLAN_IF("update_l3vlan_if"),

  REGISTER_BREAKOUT_IF("register_breakout_if"),

  DELETE_BREAKOUT_IF("delete_breakout_if");

  private String message;

  private EcCommonOperationAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcCommonOperationAction getEnumFromMessage(String message) {
    for (EcCommonOperationAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
