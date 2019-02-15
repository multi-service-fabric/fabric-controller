
package msf.mfcfc.services.filter.scenario.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.PhysicalIfFilterListEntity;

public class PhysicalIfFilterReadListResponseBody extends AbstractResponseBody {

  @SerializedName("physical_if_filters")
  private List<PhysicalIfFilterListEntity> physicalIfFilters;

  public List<PhysicalIfFilterListEntity> getPhysicalIfFilters() {
    return physicalIfFilters;
  }

  public void setPhysicalIfFilters(List<PhysicalIfFilterListEntity> physicalIfFilters) {
    this.physicalIfFilters = physicalIfFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
