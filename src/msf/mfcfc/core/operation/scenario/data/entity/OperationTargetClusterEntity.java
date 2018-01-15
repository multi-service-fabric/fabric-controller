
package msf.mfcfc.core.operation.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class OperationTargetClusterEntity {

  
  @SerializedName("cluster_id")
  private String clusterId;

  
  @SerializedName("request")
  private OperationRequestEntity request;

  
  @SerializedName("response")
  private OperationResponseEntity response;

  
  public String getClusterId() {
    return clusterId;
  }

  
  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  
  public OperationRequestEntity getRequest() {
    return request;
  }

  
  public void setRequest(OperationRequestEntity request) {
    this.request = request;
  }

  
  public OperationResponseEntity getResponse() {
    return response;
  }

  
  public void setResponse(OperationResponseEntity response) {
    this.response = response;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
