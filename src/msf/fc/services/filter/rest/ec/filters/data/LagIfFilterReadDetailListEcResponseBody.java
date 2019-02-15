
package msf.fc.services.filter.rest.ec.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.LagIfFilterDetailEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class LagIfFilterReadDetailListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("lag_if_filters")
  private List<LagIfFilterDetailEcEntity> lagIfFilters;

  public List<LagIfFilterDetailEcEntity> getLagIfFilters() {
    return lagIfFilters;
  }

  public void setLagIfFilters(List<LagIfFilterDetailEcEntity> lagIfFilters) {
    this.lagIfFilters = lagIfFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
