
package msf.mfc.common.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "async_requests_for_lower")
@NamedQuery(name = "MfcAsyncRequestsForLower.findAll", query = "SELECT m FROM MfcAsyncRequestsForLower m")
public class MfcAsyncRequestsForLower implements Serializable {
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "operation_id")
  private MfcAsyncRequest asyncRequest;

  @OneToOne(mappedBy = "asyncRequestsForLower", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private MfcAsyncRequestsForLowerRollback asyncRequestsForLowerRollback;

  public MfcAsyncRequestsForLower() {
  }

  public MfcAsyncRequestsForLower(AsyncRequestsForLower asyncRequest) {
    setCommonEntity(asyncRequest);
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

  public MfcAsyncRequest getAsyncRequest() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.asyncRequest);
  }

  public void setAsyncRequest(MfcAsyncRequest asyncRequest) {
    this.asyncRequest = asyncRequest;
  }

  public MfcAsyncRequestsForLowerRollback getAsyncRequestsForLowerRollback() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.asyncRequestsForLowerRollback);
  }

  public void setAsyncRequestsForLowerRollback(MfcAsyncRequestsForLowerRollback asyncRequestsForLowerRollback) {
    this.asyncRequestsForLowerRollback = asyncRequestsForLowerRollback;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "asyncRequest", "asyncRequestsForLowerRollbacks" }).toString();
  }

  public void setCommonEntity(AsyncRequestsForLower entity) {
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
    MfcAsyncRequest mfcAsyncRequest = new MfcAsyncRequest();
    mfcAsyncRequest.setCommonEntity(entity.getAsyncRequest());
    setAsyncRequest(mfcAsyncRequest);
  }

  public AsyncRequestsForLower getCommonEntity(AsyncRequest asyncRequest) throws MsfException {
    AsyncRequestsForLower entity = new AsyncRequestsForLower();
    entity.setId(getId().getCommonEntity());
    entity.setLastUpdateTime(getLastUpdateTime());
    entity.setOccurredTime(getOccurredTime());
    entity.setRequestBody(getRequestBody());
    entity.setRequestMethod(getRequestMethod());
    entity.setRequestUri(getRequestUri());
    entity.setResponseBody(getResponseBody());
    entity.setResponseStatusCode(getResponseStatusCode());
    if (getAsyncRequestsForLowerRollback() != null) {
      entity.setAsyncRequestsForLowerRollback(getAsyncRequestsForLowerRollback().getCommonEntity(entity));
    }
    if (asyncRequest != null) {
      entity.setAsyncRequest(asyncRequest);
    } else {
      entity.setAsyncRequest(getAsyncRequest().getCommonEntity());
    }
    return entity;
  }

}
