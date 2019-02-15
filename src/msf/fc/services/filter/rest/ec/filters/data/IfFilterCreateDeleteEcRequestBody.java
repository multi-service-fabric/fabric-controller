
package msf.fc.services.filter.rest.ec.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.IfFilterTermEcEntity;

public class IfFilterCreateDeleteEcRequestBody {

  @SerializedName("if_filter")
  private IfFilterTermEcEntity ifFilter;

  public IfFilterTermEcEntity getIfFilter() {
    return ifFilter;
  }

  public void setIfFilter(IfFilterTermEcEntity ifFilter) {
    this.ifFilter = ifFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
