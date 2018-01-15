package msf.mfcfc.common.constant;


public enum ClusterProvisioningStatus {

  
  SETTING(1),
  
  COMPLETED(2),
  
  UNSETTING(3);


  private int code;

  private ClusterProvisioningStatus(int code) {
    this.code = code;
  }

  
  public int getCode() {
    return code;
  }

  
  public static ClusterProvisioningStatus getEnumFromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (ClusterProvisioningStatus enumValue : values()) {
      if (enumValue.getCode() == code.intValue()) {
        return enumValue;
      }
    }

    return null;
  }
}
