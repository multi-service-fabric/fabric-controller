package msf.fc.node.nodes.spines.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfSpineEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("internal_options")
  private InternalOptionsSpineEntity internalOption;

  @SerializedName("minimum_links")
  private Integer minimumLinks;

  @SerializedName("speed")
  private String speed;

  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public InternalOptionsSpineEntity getInternalOption() {
    return internalOption;
  }

  public void setInternalOption(InternalOptionsSpineEntity internalOption) {
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
