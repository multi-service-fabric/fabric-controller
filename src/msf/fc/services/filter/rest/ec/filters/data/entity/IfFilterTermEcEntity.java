
package msf.fc.services.filter.rest.ec.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class IfFilterTermEcEntity {

  @SerializedName("terms")
  private List<FilterOperationTermEcEntity> terms;

  public List<FilterOperationTermEcEntity> getTerms() {
    return terms;
  }

  public void setTerms(List<FilterOperationTermEcEntity> terms) {
    this.terms = terms;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
