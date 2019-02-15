
package msf.fc.services.filter.rest.ec.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.PhysicalIfFilterDetailEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class PhysicalIfFilterReadDetailEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("physical_if_filter")
  private PhysicalIfFilterDetailEcEntity physicalIfFilter;

  public PhysicalIfFilterDetailEcEntity getPhysicalIfFilter() {
    return physicalIfFilter;
  }

  public void setPhysicalIfFilter(PhysicalIfFilterDetailEcEntity physicalIfFilter) {
    this.physicalIfFilter = physicalIfFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
