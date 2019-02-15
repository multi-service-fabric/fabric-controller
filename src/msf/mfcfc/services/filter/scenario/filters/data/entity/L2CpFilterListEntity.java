
package msf.mfcfc.services.filter.scenario.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2CpFilterListEntity {

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("term_ids")
  private List<String> termIds;

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
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
