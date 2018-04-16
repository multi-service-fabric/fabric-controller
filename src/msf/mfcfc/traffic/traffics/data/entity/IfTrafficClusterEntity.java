
package msf.mfcfc.traffic.traffics.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ClusterType;

public class IfTrafficClusterEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("type")
  private String type;

  @SerializedName("if_id")
  private String ifId;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  public ClusterType getClusterTypeEnum() {
    return ClusterType.getEnumFromMessage(type);
  }

  public void setClusterTypeEnum(ClusterType type) {
    this.type = type.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
