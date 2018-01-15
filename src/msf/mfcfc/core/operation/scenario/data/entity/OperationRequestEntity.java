package msf.mfcfc.core.operation.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.HttpMethod;


public class OperationRequestEntity {

  
  @SerializedName("uri")
  private String uri;

  
  @SerializedName("method")
  private String method;

  
  @SerializedName("body")
  private String body;

  
  public OperationRequestEntity(String uri, String method, String body) {
    super();
    this.uri = uri;
    this.method = method;
    this.body = body;
  }

  
  public String getUri() {
    return uri;
  }

  
  public void setUri(String uri) {
    this.uri = uri;
  }

  
  public String getMethod() {
    return method;
  }

  
  public void setMethod(String method) {
    this.method = method;
  }

  
  public String getBody() {
    return body;
  }

  
  public void setBody(String body) {
    this.body = body;
  }

  
  public HttpMethod getMethodEnum() {
    return HttpMethod.getEnumFromMessage(method);
  }

  
  public void setMethodEnum(HttpMethod method) {
    this.method = method.getMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
