
package msf.mfcfc.services.filter.scenario.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalIfFilterDetailEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("fabric_type")
  private String fabricType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("terms")
  private List<FilterTermEntity> terms;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getFabricType() {
    return fabricType;
  }

  public void setFabricType(String fabricType) {
    this.fabricType = fabricType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public List<FilterTermEntity> getTerms() {
    return terms;
  }

  public void setTerms(List<FilterTermEntity> terms) {
    this.terms = terms;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
