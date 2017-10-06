package msf.fc.rest.ec.node.interfaces.lag.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class CreateLagIfEcEntity {
  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("minimum_links")
  private Integer minimumLink;
  @SerializedName("link_speed")
  private String linkSpeed;

  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public Integer getMinimumLink() {
    return minimumLink;
  }

  public void setMinimumLink(Integer minimumLink) {
    this.minimumLink = minimumLink;
  }

  public String getLinkSpeed() {
    return linkSpeed;
  }

  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
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
