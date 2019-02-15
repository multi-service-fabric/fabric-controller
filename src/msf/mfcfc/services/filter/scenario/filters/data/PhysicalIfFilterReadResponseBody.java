
package msf.mfcfc.services.filter.scenario.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.entity.PhysicalIfFilterDetailEntity;

public class PhysicalIfFilterReadResponseBody extends AbstractResponseBody {

  @SerializedName("physical_if_filter")
  private PhysicalIfFilterDetailEntity physicalIfFilter;

  public PhysicalIfFilterDetailEntity getPhysicalIfFilter() {
    return physicalIfFilter;
  }

  public void setPhysicalIfFilter(PhysicalIfFilterDetailEntity physicalIfFilter) {
    this.physicalIfFilter = physicalIfFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
