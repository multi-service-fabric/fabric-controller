package msf.mfcfc.common.constant;


public enum InternalNodeType {

  
  LEAF("Leaf"),
  
  B_LEAF("B-Leaf"),
  
  SPINE("Spine"),
  
  RR("RR");


  private String message;

  private InternalNodeType(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static InternalNodeType getEnumFromMessage(String message) {
    for (InternalNodeType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
