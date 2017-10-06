package msf.fc.node.interfaces.lagifs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;


  @SerializedName("internal_options")
  private InternalOptionEntity internalOption;

  @SerializedName("minimum_links")
  private Integer minimumLinks;

  @SerializedName("speed")
  private String speed;


  @SerializedName("if_name")
  private String ifName;


  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public InternalOptionEntity getInternalOption() {
    return internalOption;
  }

  public void setInternalOption(InternalOptionEntity internalOption) {
    this.internalOption = internalOption;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
