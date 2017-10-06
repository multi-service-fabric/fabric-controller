package msf.fc.failure.logicalif.data.entity;

import com.google.gson.annotations.SerializedName;

public class LogicalIfStatusReadRequestData {

  @SerializedName("cluster_id")
  private String clusterId;

  public LogicalIfStatusReadRequestData() {

  }

  public LogicalIfStatusReadRequestData(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  @Override
  public String toString() {
    return "LogicalIfStatusReadRequestData [clusterId=" + clusterId + "]";
  }

}
