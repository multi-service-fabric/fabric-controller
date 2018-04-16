
package msf.mfc.common.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "async_requests")
@NamedQuery(name = "MfcAsyncRequest.findAll", query = "SELECT m FROM MfcAsyncRequest m")
public class MfcAsyncRequest implements Serializable {
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

  @Column(name = "response_body")
  private String responseBody;

  @Column(name = "response_status_code")
  private Integer responseStatusCode;

  private Integer status;

  @Column(name = "sub_status")
  private String subStatus;

  @OneToMany(mappedBy = "asyncRequest", cascade = CascadeType.REMOVE)
  private List<MfcAsyncRequestsForLower> asyncRequestsForLowers;

  public MfcAsyncRequest() {
  }

  public MfcAsyncRequest(AsyncRequest asyncRequest) {
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

  public List<MfcAsyncRequestsForLower> getAsyncRequestsForLowers() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.asyncRequestsForLowers);
  }

  public void setAsyncRequestsForLowers(List<MfcAsyncRequestsForLower> asyncRequestsForLowers) {
    this.asyncRequestsForLowers = asyncRequestsForLowers;
  }

  public MfcAsyncRequestsForLower addAsyncRequestsForLower(MfcAsyncRequestsForLower asyncRequestsForLower)
      throws MsfException {
    getAsyncRequestsForLowers().add(asyncRequestsForLower);
    asyncRequestsForLower.setAsyncRequest(this);

    return asyncRequestsForLower;
  }

  public MfcAsyncRequestsForLower removeAsyncRequestsForLower(MfcAsyncRequestsForLower asyncRequestsForLower)
      throws MsfException {
    getAsyncRequestsForLowers().remove(asyncRequestsForLower);
    asyncRequestsForLower.setAsyncRequest(null);

    return asyncRequestsForLower;
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
  }

  public AsyncRequest getCommonEntity() throws MsfException {
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
    List<AsyncRequestsForLower> list = new ArrayList<>();
    for (MfcAsyncRequestsForLower asyncRequestsForLower : asyncRequestsForLowers) {
      list.add(asyncRequestsForLower.getCommonEntity(asyncRequest));
    }
    asyncRequest.setAsyncRequestsForLowerList(list);
    return asyncRequest;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "asyncRequestsForLowers" })
        .toString();
  }

}
