package msf.mfcfc.common.constant;


public enum LeafNodeUpdateAction {

  
  CHG_LEAF_TYPE("chg_leaf_type"),
  
  RECOVER_NODE("recover_node");


  private String message;

  private LeafNodeUpdateAction(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static LeafNodeUpdateAction getEnumFromMessage(String message) {
    for (LeafNodeUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
