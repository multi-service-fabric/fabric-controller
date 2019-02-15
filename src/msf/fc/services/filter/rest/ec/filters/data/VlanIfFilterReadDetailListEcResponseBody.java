
package msf.fc.services.filter.rest.ec.filters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.VlanIfFilterDetailEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class VlanIfFilterReadDetailListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("vlan_if_filters")
  private List<VlanIfFilterDetailEcEntity> vlanIfFilters;

  public List<VlanIfFilterDetailEcEntity> getVlanIfFilters() {
    return vlanIfFilters;
  }

  public void setVlanIfFilters(List<VlanIfFilterDetailEcEntity> vlanIfFilters) {
    this.vlanIfFilters = vlanIfFilters;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
