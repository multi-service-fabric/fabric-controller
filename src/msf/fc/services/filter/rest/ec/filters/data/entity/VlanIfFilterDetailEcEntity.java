
package msf.fc.services.filter.rest.ec.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VlanIfFilterDetailEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("vlan_if_id")
  private String vlanIfId;

  @SerializedName("base_if")
  private BaseIfEcEntity baseIf;

  @SerializedName("terms")
  private List<FilterTermEcEntity> terms;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getVlanIfId() {
    return vlanIfId;
  }

  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  public BaseIfEcEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(BaseIfEcEntity baseIf) {
    this.baseIf = baseIf;
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
