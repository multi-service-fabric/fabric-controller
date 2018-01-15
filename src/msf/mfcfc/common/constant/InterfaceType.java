package msf.mfcfc.common.constant;


public enum InterfaceType {

  
  LAG_IF("lag-if"),
  
  PHYSICAL_IF("physical-if"),
  
  BREAKOUT_IF("breakout-if"),
  
  VLAN_IF("vlan-if");


  private String message;

  private InterfaceType(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static InterfaceType getEnumFromMessage(String message) {
    for (InterfaceType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
