package msf.mfcfc.core.scenario;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ClusterRequestResult;
import msf.mfcfc.common.constant.ErrorResponseDataConsistency;


public class TargetClusterEntity {


  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("request_results")
  private String requestResults;


  @SerializedName("error_code")
  private String errorCode;


  @SerializedName("error_message")
  private String errorMessage;


  @SerializedName("data_consistency")
  private String dataConsistency;

  
  public TargetClusterEntity(String clusterId, String requestResults, String errorCode, String errorMessage,
      String dataConsistency) {
    super();
    this.clusterId = clusterId;
    this.requestResults = requestResults;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.dataConsistency = dataConsistency;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getRequestResults() {
    return requestResults;
  }

  public void setRequestResults(String requestResults) {
    this.requestResults = requestResults;
  }

  public ClusterRequestResult getRequestResultsEnum() {
    return ClusterRequestResult.getEnumFromMessage(requestResults);
  }

  public void setRequestResultsEnum(ClusterRequestResult clusterRequestResult) {
    this.requestResults = clusterRequestResult.getMessage();
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getDataConsistency() {
    return dataConsistency;
  }

  public ErrorResponseDataConsistency getDataConsistencyEnum() {
    return ErrorResponseDataConsistency.getEnumFromMessage(dataConsistency);
  }

  public void setDataConsistency(String dataConsistency) {
    this.dataConsistency = dataConsistency;
  }

  public void setDataConsistencyEnum(ErrorResponseDataConsistency errorResponseDataConsistency) {
    this.dataConsistency = errorResponseDataConsistency.getMessage();
  }

}
