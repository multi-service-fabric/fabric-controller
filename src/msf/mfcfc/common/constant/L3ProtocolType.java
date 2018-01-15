package msf.mfcfc.common.constant;


public enum L3ProtocolType {

  
  BGP("bgp"),
  
  STATIC("static"),
  
  VRRP("vrrp");


  private String message;

  private L3ProtocolType(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static L3ProtocolType getEnumFromMessage(String message) {
    for (L3ProtocolType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
