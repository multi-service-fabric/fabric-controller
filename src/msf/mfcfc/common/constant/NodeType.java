package msf.mfcfc.common.constant;


public enum NodeType {

  
  LEAF(1, "leaf", "leafs"),
  
  SPINE(2, "spine", "spines"),
  
  RR(3, "rr", "rrs");


  private int code;


  private String singularMessage;

  private String pluralMessage;

  private NodeType(int code, String singularMessage, String pluralMessage) {
    this.code = code;
    this.singularMessage = singularMessage;
    this.pluralMessage = pluralMessage;
  }

  
  public int getCode() {
    return code;
  }

  
  public String getSingularMessage() {
    return singularMessage;
  }

  
  public String getPluralMessage() {
    return pluralMessage;
  }

  
  public static NodeType getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (NodeType enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static NodeType getEnumFromSingularMessage(String message) {
    for (NodeType enumValue : values()) {
      if (enumValue.getSingularMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static NodeType getEnumFromPluralMessage(String message) {
    for (NodeType enumValue : values()) {
      if (enumValue.getPluralMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
