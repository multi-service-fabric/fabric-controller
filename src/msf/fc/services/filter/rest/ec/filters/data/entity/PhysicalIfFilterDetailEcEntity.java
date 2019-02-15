
package msf.fc.services.filter.rest.ec.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalIfFilterDetailEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("terms")
  private List<FilterTermEcEntity> terms;

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

  public List<FilterTermEcEntity> getTerms() {
    return terms;
  }

  public void setTerms(List<FilterTermEcEntity> terms) {
    this.terms = terms;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
