package msf.mfcfc.common.data;


public class AsyncRequestsForLowerPK {

  private Integer clusterId;

  private String requestOperationId;

  public AsyncRequestsForLowerPK() {
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
}
