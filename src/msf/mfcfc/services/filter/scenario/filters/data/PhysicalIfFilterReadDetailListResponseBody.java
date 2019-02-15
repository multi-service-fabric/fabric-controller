
package msf.mfcfc.services.filter.scenario.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.PhysicalIfFilterDetailEntity;

public class PhysicalIfFilterReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("physical_if_filters")
  private List<PhysicalIfFilterDetailEntity> physicalIfFilters;

  public List<PhysicalIfFilterDetailEntity> getPhysicalIfFilters() {
    return physicalIfFilters;
  }

  public void setPhysicalIfFilters(List<PhysicalIfFilterDetailEntity> physicalIfFilters) {
    this.physicalIfFilters = physicalIfFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
