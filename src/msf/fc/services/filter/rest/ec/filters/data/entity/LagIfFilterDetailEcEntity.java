
package msf.fc.services.filter.rest.ec.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfFilterDetailEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("lag_member")
  private LagIfMemberEcEntity lagMember;

  @SerializedName("terms")
  private List<FilterTermEcEntity> terms;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public LagIfMemberEcEntity getLagMember() {
    return lagMember;
  }

  public void setLagMember(LagIfMemberEcEntity lagMember) {
    this.lagMember = lagMember;
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
