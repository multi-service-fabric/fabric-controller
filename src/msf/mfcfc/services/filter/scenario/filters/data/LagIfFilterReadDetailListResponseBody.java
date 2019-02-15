
package msf.mfcfc.services.filter.scenario.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.LagIfFilterDetailEntity;

public class LagIfFilterReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("lag_if_filters")
  private List<LagIfFilterDetailEntity> lagIfFilters;

  public List<LagIfFilterDetailEntity> getLagIfFilters() {
    return lagIfFilters;
  }

  public void setLagIfFilters(List<LagIfFilterDetailEntity> lagIfFilters) {
    this.lagIfFilters = lagIfFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
