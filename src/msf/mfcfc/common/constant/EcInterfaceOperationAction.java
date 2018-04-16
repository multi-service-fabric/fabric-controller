
package msf.mfcfc.common.constant;

public enum EcInterfaceOperationAction {

  ADD_INTER_CLUSTER_LINK("add_inter_cluster_link"),

  DEL_INTER_CLUSTER_LINK("del_inter_cluster_link");

  private String message;

  private EcInterfaceOperationAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static EcInterfaceOperationAction getEnumFromMessage(String message) {
    for (EcInterfaceOperationAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
