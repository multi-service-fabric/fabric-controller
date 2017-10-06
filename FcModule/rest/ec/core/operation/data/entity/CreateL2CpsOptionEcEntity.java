package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class CreateL2CpsOptionEcEntity {

  @SerializedName("cps")
  private List<CreateL2CpEcEntity> l2CpList;
  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("ipv4_multicast_address")
  private String ipv4MulticastAddress;

  public List<CreateL2CpEcEntity> getL2CpList() {
    return l2CpList;
  }

  public void setL2CpList(List<CreateL2CpEcEntity> l2CpList) {
    this.l2CpList = l2CpList;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public String getIpv4MulticastAddress() {
    return ipv4MulticastAddress;
  }

  public void setIpv4MulticastAddress(String ipv4MulticastAddress) {
    this.ipv4MulticastAddress = ipv4MulticastAddress;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
