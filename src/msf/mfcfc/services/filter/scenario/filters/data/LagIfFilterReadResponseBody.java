
package msf.mfcfc.services.filter.scenario.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.LagIfFilterDetailEntity;

public class LagIfFilterReadResponseBody extends AbstractResponseBody {

  @SerializedName("lag_if_filter")
  private LagIfFilterDetailEntity lagIfFilter;

  public LagIfFilterDetailEntity getLagIfFilter() {
    return lagIfFilter;
  }

  public void setLagIfFilter(LagIfFilterDetailEntity lagIfFilter) {
    this.lagIfFilter = lagIfFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
