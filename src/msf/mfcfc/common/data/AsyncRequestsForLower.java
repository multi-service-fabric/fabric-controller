
package msf.mfcfc.common.data;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class AsyncRequestsForLower {

  private AsyncRequestsForLowerPK id;

  private Timestamp lastUpdateTime;

  private Timestamp occurredTime;

  private String requestBody;

  private String requestMethod;

  private String requestUri;

  private String responseBody;

  private Integer responseStatusCode;

  private AsyncRequest asyncRequest;

  private AsyncRequestsForLowerRollback asyncRequestsForLowerRollback;

  public AsyncRequestsForLower() {
  }

  public AsyncRequestsForLowerPK getId() {
    return id;
  }

  public void setId(AsyncRequestsForLowerPK id) {
    this.id = id;
  }

  public Timestamp getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Timestamp lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public Timestamp getOccurredTime() {
    return occurredTime;
  }

  public void setOccurredTime(Timestamp occurredTime) {
    this.occurredTime = occurredTime;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  public Integer getResponseStatusCode() {
    return responseStatusCode;
  }

  public void setResponseStatusCode(Integer responseStatusCode) {
    this.responseStatusCode = responseStatusCode;
  }

  public AsyncRequest getAsyncRequest() {
    return asyncRequest;
  }

  public void setAsyncRequest(AsyncRequest asyncRequest) {
    this.asyncRequest = asyncRequest;
  }

  public AsyncRequestsForLowerRollback getAsyncRequestsForLowerRollback() {
    return asyncRequestsForLowerRollback;
  }

  public void setAsyncRequestsForLowerRollback(AsyncRequestsForLowerRollback asyncRequestsForLowerRollback) {
    this.asyncRequestsForLowerRollback = asyncRequestsForLowerRollback;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "asyncRequest", "asyncRequestsForLowerRollbacks" }).toString();
  }

}
