
package msf.mfcfc.core.operation.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationTargetClusterDetailEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("request")
  private OperationRequestDetailEntity request;

  @SerializedName("response")
  private OperationResponseDetailEntity response;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public OperationRequestDetailEntity getRequest() {
    return request;
  }

  public void setRequest(OperationRequestDetailEntity request) {
    this.request = request;
  }

  public OperationResponseDetailEntity getResponse() {
    return response;
  }

  public void setResponse(OperationResponseDetailEntity response) {
    this.response = response;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
