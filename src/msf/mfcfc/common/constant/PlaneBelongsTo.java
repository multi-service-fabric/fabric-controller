package msf.mfcfc.common.constant;


public enum PlaneBelongsTo {

  
  A_PLANE(1, 1),
  
  B_PLANE(2, 2);


  private int code;


  private int message;

  private PlaneBelongsTo(int code, int message) {
    this.code = code;
    this.message = message;
  }

  
  public int getCode() {
    return code;
  }

  
  public int getMessage() {
    return message;
  }

  
  public static PlaneBelongsTo getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (PlaneBelongsTo enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }

    return null;
  }

  
  public static PlaneBelongsTo getEnumFromMessage(Integer message) {
    if (message == null) {
      return null;
    }
    for (PlaneBelongsTo enumValue : values()) {
      if (enumValue.getMessage() == message.intValue()) {
        return enumValue;
      }
    }

    return null;
  }

}
