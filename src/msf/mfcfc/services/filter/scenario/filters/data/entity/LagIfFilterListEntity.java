
package msf.mfcfc.services.filter.scenario.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfFilterListEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("term_ids")
  private List<String> termIds;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
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
