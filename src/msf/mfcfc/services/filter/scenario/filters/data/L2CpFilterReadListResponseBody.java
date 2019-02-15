
package msf.mfcfc.services.filter.scenario.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.L2CpFilterListEntity;

public class L2CpFilterReadListResponseBody extends AbstractResponseBody {

  @SerializedName("cp_filters")
  private List<L2CpFilterListEntity> cpFilters;

  public List<L2CpFilterListEntity> getCpFilters() {
    return cpFilters;
  }

  public void setCpFilters(List<L2CpFilterListEntity> cpFilters) {
    this.cpFilters = cpFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
