package msf.mfcfc.common.constant;


public enum ClusterLinkIfPortStatus {

  
  ENABLE(true, true, "enable"),
  
  DISABLE(false, false, "disable");


  private boolean code;


  private boolean boolMessage;


  private String stringMessage;

  private ClusterLinkIfPortStatus(boolean code, boolean boolMessage, String stringMessage) {
    this.code = code;
    this.boolMessage = boolMessage;
    this.stringMessage = stringMessage;
  }

  
  public boolean isCode() {
    return code;
  }

  
  public boolean isBoolMessage() {
    return boolMessage;
  }

  
  public String getStringMessage() {
    return stringMessage;
  }

  
  public static ClusterLinkIfPortStatus getEnumFromCode(Boolean code) {
    if (code == null) {
      return null;
    }
    for (ClusterLinkIfPortStatus enumValue : values()) {
      if (enumValue.isCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static ClusterLinkIfPortStatus getEnumFromMessage(Boolean message) {
    if (message == null) {
      return null;
    }

    for (ClusterLinkIfPortStatus enumValue : values()) {
      if (enumValue.isBoolMessage() == message) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static ClusterLinkIfPortStatus getEnumFromMessage(String message) {
    for (ClusterLinkIfPortStatus enumValue : values()) {
      if (enumValue.getStringMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
