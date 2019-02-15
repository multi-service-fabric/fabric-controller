
package msf.fc.services.filter.rest.ec.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.filter.rest.ec.filters.data.entity.VlanIfFilterDetailEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class VlanIfFilterReadDetailEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("vlan_if_filter")
  private VlanIfFilterDetailEcEntity vlanIfFilter;

  public VlanIfFilterDetailEcEntity getVlanIfFilter() {
    return vlanIfFilter;
  }

  public void setVlanIfFilter(VlanIfFilterDetailEcEntity vlanIfFilter) {
    this.vlanIfFilter = vlanIfFilter;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
