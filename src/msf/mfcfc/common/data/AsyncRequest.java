
package msf.mfcfc.common.data;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.HttpMethod;

public class AsyncRequest {
  private String operationId;

  private Timestamp lastUpdateTime;

  private String notificationIpAddress;

  private Integer notificationPortNumber;

  private Timestamp occurredTime;

  private String requestBody;

  private String requestMethod;

  private String requestUri;

  private String responseBody;

  private Integer responseStatusCode;

  private Integer status;

  private String subStatus;

  private List<AsyncRequestsForLower> asyncRequestsForLowerList = null;

  public AsyncRequest() {
  }

  public String getOperationId() {
    return this.operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public Timestamp getLastUpdateTime() {
    return this.lastUpdateTime;
  }

  public void setLastUpdateTime(Timestamp lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public String getNotificationIpAddress() {
    return notificationIpAddress;
  }

  public void setNotificationIpAddress(String notificationIpAddress) {
    this.notificationIpAddress = notificationIpAddress;
  }

  public Integer getNotificationPortNumber() {
    return notificationPortNumber;
  }

  public void setNotificationPortNumber(Integer notificationPortNumber) {
    this.notificationPortNumber = notificationPortNumber;
  }

  public Timestamp getOccurredTime() {
    return this.occurredTime;
  }

  public void setOccurredTime(Timestamp occurredTime) {
    this.occurredTime = occurredTime;
  }

  public String getRequestBody() {
    return this.requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  public String getRequestMethod() {
    return this.requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public HttpMethod getRequestMethodEnum() {
    return HttpMethod.getEnumFromMessage(requestMethod);
  }

  public void setRequestMethodEnum(HttpMethod requestMethod) {
    this.requestMethod = requestMethod.getMessage();
  }

  public String getRequestUri() {
    return this.requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  public String getResponseBody() {
    return this.responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  public Integer getResponseStatusCode() {
    return this.responseStatusCode;
  }

  public void setResponseStatusCode(Integer responseStatusCode) {
    this.responseStatusCode = responseStatusCode;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public AsyncProcessStatus getStatusEnum() {
    return AsyncProcessStatus.getEnumFromCode(this.status);
  }

  public void setStatusEnum(AsyncProcessStatus status) {
    this.status = status.getCode();
  }

  public String getSubStatus() {
    return this.subStatus;
  }

  public void setSubStatus(String subStatus) {
    this.subStatus = subStatus;
  }

  public List<AsyncRequestsForLower> getAsyncRequestsForLowerList() {
    return asyncRequestsForLowerList;
  }

  public void setAsyncRequestsForLowerList(List<AsyncRequestsForLower> asyncRequestsForLowerList) {
    this.asyncRequestsForLowerList = asyncRequestsForLowerList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
