
package msf.mfcfc.services.filter.scenario.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalIfFilterListEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("term_ids")
  private List<String> termIds;

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public List<String> getTermIds() {
    return termIds;
  }

  public void setTermIds(List<String> termIds) {
    this.termIds = termIds;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
