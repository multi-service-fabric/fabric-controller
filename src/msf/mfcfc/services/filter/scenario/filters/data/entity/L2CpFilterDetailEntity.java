
package msf.mfcfc.services.filter.scenario.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2CpFilterDetailEntity {

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("terms")
  private List<FilterTermEntity> terms;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
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
