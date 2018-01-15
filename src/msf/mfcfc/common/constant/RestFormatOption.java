package msf.mfcfc.common.constant;


public enum RestFormatOption {

  
  LIST("list"),
  
  DETAIL_LIST("detail-list");


  private String message;

  private RestFormatOption(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static RestFormatOption getEnumFromMessage(String message) {
    for (RestFormatOption enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
