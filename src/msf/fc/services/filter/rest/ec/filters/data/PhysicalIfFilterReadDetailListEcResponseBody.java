
package msf.fc.services.filter.rest.ec.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.PhysicalIfFilterDetailEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class PhysicalIfFilterReadDetailListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("physical_if_filters")
  private List<PhysicalIfFilterDetailEcEntity> physicalIfFilters;

  public List<PhysicalIfFilterDetailEcEntity> getPhysicalIfFilters() {
    return physicalIfFilters;
  }

  public void setPhysicalIfFilters(List<PhysicalIfFilterDetailEcEntity> physicalIfFilters) {
    this.physicalIfFilters = physicalIfFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
