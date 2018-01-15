package msf.mfcfc.common.constant;


public enum HttpMethod {
  
  POST("POST"),
  
  GET("GET"),
  
  PUT("PUT"),
  
  DELETE("DELETE"),
  
  PATCH("PATCH");


  private String message;

  private HttpMethod(String message) {
    this.message = message;
  }

  
  public String getMessage() {
    return message;
  }

  
  public static HttpMethod getEnumFromMessage(String message) {
    for (HttpMethod enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }
}
