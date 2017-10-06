package msf.fc.rest.ec.traffic.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class TrafficValueEcEntity {
  @SerializedName("l3_cps")
  private List<L3CpEcEntity> l3CpList;

  @SerializedName("l2_cps")
  private List<L2CpEcEntity> l2CpList;

  @SerializedName("internal_links")
  private List<InternalLinkEcEntity> internalLinkList;

  public List<L3CpEcEntity> getL3CpList() {
    return l3CpList;
  }

  public void setL3CpList(List<L3CpEcEntity> l3CpList) {
    this.l3CpList = l3CpList;
  }

  public List<L2CpEcEntity> getL2CpList() {
    return l2CpList;
  }

  public void setL2CpList(List<L2CpEcEntity> l2CpList) {
    this.l2CpList = l2CpList;
  }

  public List<InternalLinkEcEntity> getInternalLinkList() {
    return internalLinkList;
  }

  public void setInternalLinkList(List<InternalLinkEcEntity> internalLinkList) {
    this.internalLinkList = internalLinkList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
