package msf.mfcfc.common.constant;


public enum LeafType {

  
  BORDER_LEAF(1, "BL"),
  
  IP_VPN_LEAF(2, "IL"),
  
  ETHERNET_VPN_LEAF(3, "EL");


  private int code;


  private String message;

  private LeafType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  
  public int getCode() {
    return code;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static LeafType getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (LeafType enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static LeafType getEnumFromMessage(String message) {
    for (LeafType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
