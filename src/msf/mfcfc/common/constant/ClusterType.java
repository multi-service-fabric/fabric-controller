package msf.mfcfc.common.constant;


public enum ClusterType {

  
  EDGE_POINT("edge_point"),
  
  CLUSTER_LINK_IF("cluster_link-if"),
  
  INTERNAL("internal");


  private String message;

  private ClusterType(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static ClusterType getEnumFromMessage(String message) {
    for (ClusterType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
