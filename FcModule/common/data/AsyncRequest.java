package msf.fc.common.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.AsyncProcessStatus;

@Entity
@Table(name = "async_requests")
@NamedQuery(name = "AsyncRequest.findAll", query = "SELECT a FROM AsyncRequest a")
public class AsyncRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "operation_id")
  private String operationId;

  @Column(name = "last_update_time")
  private Timestamp lastUpdateTime;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}