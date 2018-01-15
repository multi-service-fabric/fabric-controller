package msf.mfcfc.common.constant;


public enum EcNodeOperationAction {

  
  ADD_NODE("add_node"),
  
  DEL_NODE("del_node"),
  
  UPDATE_NODE("update_node");


  private String message;

  private EcNodeOperationAction(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static EcNodeOperationAction getEnumFromMessage(String message) {
    for (EcNodeOperationAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
