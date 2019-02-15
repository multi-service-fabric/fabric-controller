
package msf.mfcfc.services.filter.scenario.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.L2CpFilterDetailEntity;

public class L2CpFilterReadResponseBody extends AbstractResponseBody {

  @SerializedName("cp_filter")
  private L2CpFilterDetailEntity cpFilter;

  public L2CpFilterDetailEntity getCpFilter() {
    return cpFilter;
  }

  public void setCpFilter(L2CpFilterDetailEntity cpFilter) {
    this.cpFilter = cpFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
