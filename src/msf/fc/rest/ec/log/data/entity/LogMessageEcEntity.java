package msf.fc.rest.ec.log.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LogMessageEcEntity {

  
  @SerializedName("message")
  private String message;

  
  public String getMessage() {
    return message;
  }

  
  public void setMessage(String message) {
    this.message = message;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
