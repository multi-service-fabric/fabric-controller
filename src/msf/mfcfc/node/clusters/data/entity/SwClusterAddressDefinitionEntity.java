
package msf.mfcfc.node.clusters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SwClusterAddressDefinitionEntity {

  @SerializedName("interface_start_address")
  private String interfaceStartAddress;

  @SerializedName("loopback_start_address")
  private String loopbackStartAddress;

  @SerializedName("management_start_address")
  private String managementStartAddress;

  @SerializedName("management_address_prefix")
  private Integer managementAddressPrefix;

  public String getInterfaceStartAddress() {
    return interfaceStartAddress;
  }

  public void setInterfaceStartAddress(String interfaceStartAddress) {
    this.interfaceStartAddress = interfaceStartAddress;
  }

  public String getLoopbackStartAddress() {
    return loopbackStartAddress;
  }

  public void setLoopbackStartAddress(String loopbackStartAddress) {
    this.loopbackStartAddress = loopbackStartAddress;
  }

  public String getManagementStartAddress() {
    return managementStartAddress;
  }

  public void setManagementStartAddress(String managementStartAddress) {
    this.managementStartAddress = managementStartAddress;
  }

  public Integer getManagementAddressPrefix() {
    return managementAddressPrefix;
  }

  public void setManagementAddressPrefix(Integer managementAddressPrefix) {
    this.managementAddressPrefix = managementAddressPrefix;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
