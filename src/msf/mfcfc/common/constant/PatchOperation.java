package msf.mfcfc.common.constant;


public enum PatchOperation {

  
  ADD("add"),
  
  REMOVE("remove"),
  
  REPLACE("replace"),
  
  MOVE("move"),
  
  COPY("copy"),
  
  TEST("test");


  private String message;

  private PatchOperation(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static PatchOperation getEnumFromMessage(String message) {
    for (PatchOperation enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
