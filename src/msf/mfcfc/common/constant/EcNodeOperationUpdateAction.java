package msf.mfcfc.common.constant;


public enum EcNodeOperationUpdateAction {

  
  CHG_B_LEAF("chg_b_leaf"),
  
  CHG_LEAF("chg_leaf"),
  
  ADD_OSPF_ROUTE("add_ospf_route"),
  
  DELETE_OSPF_ROUTE("delete_ospf_route");


  private String message;

  private EcNodeOperationUpdateAction(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static EcNodeOperationUpdateAction getEnumFromMessage(String message) {
    for (EcNodeOperationUpdateAction enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
