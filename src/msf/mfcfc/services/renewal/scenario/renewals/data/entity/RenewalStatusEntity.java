
package msf.mfcfc.services.renewal.scenario.renewals.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.RenewalStatusType;

public class RenewalStatusEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("renewal_status")
  private String renewalStatus;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getRenewalStatus() {
    return renewalStatus;
  }

  public void setRenewalStatus(String renewalStatus) {
    this.renewalStatus = renewalStatus;
  }

  public RenewalStatusType getRenewalStatusEnum() {
    return RenewalStatusType.getEnumFromMessage(renewalStatus);
  }

  public void setRenewalStatusEnum(RenewalStatusType renewalStatus) {
    this.renewalStatus = renewalStatus.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
