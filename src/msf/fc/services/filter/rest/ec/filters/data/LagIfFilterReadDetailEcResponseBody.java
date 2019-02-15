
package msf.fc.services.filter.rest.ec.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.LagIfFilterDetailEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class LagIfFilterReadDetailEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("lag_if_filter")
  private LagIfFilterDetailEcEntity lagIfFilter;

  public LagIfFilterDetailEcEntity getLagIfFilter() {
    return lagIfFilter;
  }

  public void setLagIfFilter(LagIfFilterDetailEcEntity lagIfFilter) {
    this.lagIfFilter = lagIfFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
