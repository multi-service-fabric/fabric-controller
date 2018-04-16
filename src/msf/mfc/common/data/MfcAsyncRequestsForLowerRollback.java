
package msf.mfc.common.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.data.AsyncRequestsForLowerRollback;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "async_requests_for_lower_rollback")
@NamedQuery(name = "MfcAsyncRequestsForLowerRollback.findAll", query = "SELECT m FROM MfcAsyncRequestsForLowerRollback m")
public class MfcAsyncRequestsForLowerRollback implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private MfcAsyncRequestsForLowerPK id;

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

  @Column(name = "rollback_operation_id")
  private String rollbackOperationId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", insertable = false, updatable = false),
      @JoinColumn(name = "request_operation_id", referencedColumnName = "request_operation_id", insertable = false, updatable = false) })
  private MfcAsyncRequestsForLower asyncRequestsForLower;

  public MfcAsyncRequestsForLowerRollback() {
  }

  public MfcAsyncRequestsForLowerRollback(AsyncRequestsForLowerRollback entity) {
    setCommonEntity(entity);
  }

  public MfcAsyncRequestsForLowerPK getId() {
    return this.id;
  }

  public void setId(MfcAsyncRequestsForLowerPK id) {
    this.id = id;
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

  public String getRollbackOperationId() {
    return this.rollbackOperationId;
  }

  public void setRollbackOperationId(String rollbackOperationId) {
    this.rollbackOperationId = rollbackOperationId;
  }

  public MfcAsyncRequestsForLower getAsyncRequestsForLower() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.asyncRequestsForLower);
  }

  public void setAsyncRequestsForLower(MfcAsyncRequestsForLower asyncRequestsForLower) {
    this.asyncRequestsForLower = asyncRequestsForLower;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "asyncRequestsForLower" })
        .toString();
  }

  public void setCommonEntity(AsyncRequestsForLowerRollback entity) {
    MfcAsyncRequestsForLowerPK pk = new MfcAsyncRequestsForLowerPK();
    pk.setCommonEntity(entity.getId());
    setId(pk);
    setLastUpdateTime(entity.getLastUpdateTime());
    setOccurredTime(entity.getOccurredTime());
    setRequestBody(entity.getRequestBody());
    setRequestMethod(entity.getRequestMethod());
    setRequestUri(entity.getRequestUri());
    setResponseBody(entity.getResponseBody());
    setResponseStatusCode(entity.getResponseStatusCode());
    setRollbackOperationId(entity.getRollbackOperationId());
  }

  public AsyncRequestsForLowerRollback getCommonEntity(AsyncRequestsForLower asyncRequestsForLower)
      throws MsfException {
    AsyncRequestsForLowerRollback entity = new AsyncRequestsForLowerRollback();
    entity.setId(getId().getCommonEntity());
    entity.setLastUpdateTime(getLastUpdateTime());
    entity.setOccurredTime(getOccurredTime());
    entity.setRequestBody(getRequestBody());
    entity.setRequestMethod(getRequestMethod());
    entity.setRequestUri(getRequestUri());
    entity.setResponseBody(getResponseBody());
    entity.setResponseStatusCode(getResponseStatusCode());
    entity.setRollbackOperationId(getRollbackOperationId());
    if (asyncRequestsForLower != null) {
      entity.setAsyncRequestsForLower(asyncRequestsForLower);
    } else {
      entity.setAsyncRequestsForLower(getAsyncRequestsForLower().getCommonEntity(null));
    }

    return entity;
  }

}
