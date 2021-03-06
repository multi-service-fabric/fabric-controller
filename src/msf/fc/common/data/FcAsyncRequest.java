
package msf.fc.common.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.data.AsyncRequest;

@Entity
@Table(name = "async_requests")
@NamedQuery(name = "FcAsyncRequest.findAll", query = "SELECT f FROM FcAsyncRequest f")
public class FcAsyncRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "operation_id")
  private String operationId;

  @Column(name = "last_update_time")
  private Timestamp lastUpdateTime;

  @Column(name = "notification_ip_address")
  private String notificationIpAddress;

  @Column(name = "notification_port_number")
  private Integer notificationPortNumber;

  @Column(name = "occurred_time")
  private Timestamp occurredTime;

  @Column(name = "request_body")
  private String requestBody;

  @Column(name = "request_method")
  private String requestMethod;

  @Column(name = "request_uri")
  private String requestUri;

  @Column(name = "reservation_time")
  private Timestamp reservationTime;

  @Column(name = "response_body")
  private String responseBody;

  @Column(name = "response_status_code")
  private Integer responseStatusCode;

  @Column(name = "start_time")
  private Timestamp startTime;

  private Integer status;

  @Column(name = "sub_status")
  private String subStatus;

  public FcAsyncRequest() {
  }

  public FcAsyncRequest(AsyncRequest asyncRequest) {
    setCommonEntity(asyncRequest);
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
    return this.notificationIpAddress;
  }

  public void setNotificationIpAddress(String notificationIpAddress) {
    this.notificationIpAddress = notificationIpAddress;
  }

  public Integer getNotificationPortNumber() {
    return this.notificationPortNumber;
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

  public Timestamp getReservationTime() {
    return this.reservationTime;
  }

  public void setReservationTime(Timestamp reservationTime) {
    this.reservationTime = reservationTime;
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

  public Timestamp getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Timestamp startTime) {
    this.startTime = startTime;
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

  public void setCommonEntity(AsyncRequest asyncRequest) {
    setLastUpdateTime(asyncRequest.getLastUpdateTime());
    setNotificationIpAddress(asyncRequest.getNotificationIpAddress());
    setNotificationPortNumber(asyncRequest.getNotificationPortNumber());
    setOccurredTime(asyncRequest.getOccurredTime());
    setOperationId(asyncRequest.getOperationId());
    setRequestBody(asyncRequest.getRequestBody());
    setRequestMethod(asyncRequest.getRequestMethod());
    setRequestUri(asyncRequest.getRequestUri());
    setResponseBody(asyncRequest.getResponseBody());
    setResponseStatusCode(asyncRequest.getResponseStatusCode());
    setStatus(asyncRequest.getStatus());
    setSubStatus(asyncRequest.getSubStatus());
    setReservationTime(asyncRequest.getReservationTime());
    setStartTime(asyncRequest.getStartTime());
  }

  public AsyncRequest getCommonEntity() {
    AsyncRequest asyncRequest = new AsyncRequest();
    asyncRequest.setLastUpdateTime(getLastUpdateTime());
    asyncRequest.setNotificationIpAddress(getNotificationIpAddress());
    asyncRequest.setNotificationPortNumber(getNotificationPortNumber());
    asyncRequest.setOccurredTime(getOccurredTime());
    asyncRequest.setOperationId(getOperationId());
    asyncRequest.setRequestBody(getRequestBody());
    asyncRequest.setRequestMethod(getRequestMethod());
    asyncRequest.setRequestUri(getRequestUri());
    asyncRequest.setResponseBody(getResponseBody());
    asyncRequest.setResponseStatusCode(getResponseStatusCode());
    asyncRequest.setStatus(getStatus());
    asyncRequest.setSubStatus(getSubStatus());
    asyncRequest.setReservationTime(getReservationTime());
    asyncRequest.setStartTime(getStartTime());
    return asyncRequest;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
