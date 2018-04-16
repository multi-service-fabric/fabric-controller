
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import msf.mfcfc.common.data.AsyncRequestsForLowerPK;

@Embeddable
public class MfcAsyncRequestsForLowerPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "cluster_id")
  private Integer clusterId;

  @Column(name = "request_operation_id")
  private String requestOperationId;

  public MfcAsyncRequestsForLowerPK() {
  }

  public MfcAsyncRequestsForLowerPK(AsyncRequestsForLowerPK pk) {
    setCommonEntity(pk);
  }

  public Integer getClusterId() {
    return this.clusterId;
  }

  public void setClusterId(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public String getRequestOperationId() {
    return this.requestOperationId;
  }

  public void setRequestOperationId(String requestOperationId) {
    this.requestOperationId = requestOperationId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MfcAsyncRequestsForLowerPK)) {
      return false;
    }
    MfcAsyncRequestsForLowerPK castOther = (MfcAsyncRequestsForLowerPK) other;
    return this.clusterId.equals(castOther.clusterId) && this.requestOperationId.equals(castOther.requestOperationId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.clusterId.hashCode();
    hash = hash * prime + this.requestOperationId.hashCode();

    return hash;
  }

  public void setCommonEntity(AsyncRequestsForLowerPK pk) {
    setClusterId(pk.getClusterId());
    setRequestOperationId(pk.getRequestOperationId());
  }

  public AsyncRequestsForLowerPK getCommonEntity() {
    AsyncRequestsForLowerPK pk = new AsyncRequestsForLowerPK();
    pk.setClusterId(getClusterId());
    pk.setRequestOperationId(getRequestOperationId());

    return pk;
  }
}
