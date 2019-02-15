
package msf.mfcfc.services.filter.scenario.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.L2CpFilterDetailEntity;

public class L2CpFilterReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("cp_filters")
  private List<L2CpFilterDetailEntity> cpFilters;

  public List<L2CpFilterDetailEntity> getCpFilters() {
    return cpFilters;
  }

  public void setCpFilters(List<L2CpFilterDetailEntity> cpFilters) {
    this.cpFilters = cpFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
