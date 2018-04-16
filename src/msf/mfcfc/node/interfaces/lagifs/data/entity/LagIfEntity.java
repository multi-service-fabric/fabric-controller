
package msf.mfcfc.node.interfaces.lagifs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("internal_options")
  private LagIfInternalOptionEntity internalOptions;

  @SerializedName("minimum_links")
  private Integer minimumLinks;

  @SerializedName("speed")
  private String speed;

  @SerializedName("if_name")
  private String ifName;

  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  @SerializedName("breakout_if_ids")
  private List<String> breakoutIfIdList;

  @SerializedName("qos")
  private LagIfQosEntity qos;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public LagIfInternalOptionEntity getInternalOptions() {
    return internalOptions;
  }

  public void setInternalOptions(LagIfInternalOptionEntity internalOptions) {
    this.internalOptions = internalOptions;
  }

  public Integer getMinimumLinks() {
    return minimumLinks;
  }

  public void setMinimumLinks(Integer minimumLinks) {
    this.minimumLinks = minimumLinks;
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public String getIfName() {
    return ifName;
  }

  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  public List<String> getPhysicalIfIdList() {
    return physicalIfIdList;
  }

  public void setPhysicalIfIdList(List<String> physicalIfIdList) {
    this.physicalIfIdList = physicalIfIdList;
  }

  public List<String> getBreakoutIfIdList() {
    return breakoutIfIdList;
  }

  public void setBreakoutIfIdList(List<String> breakoutIfIdList) {
    this.breakoutIfIdList = breakoutIfIdList;
  }

  public LagIfQosEntity getQos() {
    return qos;
  }

  public void setQos(LagIfQosEntity qos) {
    this.qos = qos;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
