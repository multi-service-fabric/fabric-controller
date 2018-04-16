
package msf.mfcfc.core.operation.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationRequestDetailEntity {

  @SerializedName("uri")
  private String uri;

  @SerializedName("method")
  private String method;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
