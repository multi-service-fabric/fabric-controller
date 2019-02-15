
package msf.mfcfc.services.filter.scenario.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.LagIfFilterListEntity;

public class LagIfFilterReadListResponseBody extends AbstractResponseBody {

  @SerializedName("lag_if_filters")
  private List<LagIfFilterListEntity> lagIfFilter;

  public List<LagIfFilterListEntity> getLagIfFilter() {
    return lagIfFilter;
  }

  public void setLagIfFilter(List<LagIfFilterListEntity> lagIfFilter) {
    this.lagIfFilter = lagIfFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
