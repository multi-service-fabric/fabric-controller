package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfExpansionEcEntity {
  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("physical_ifs")
  private List<PhysicalIfIdEcEntity> physicalIfList;

  @SerializedName("minimum_links")
  private Integer minimumLinks;

  @SerializedName("link_speed")
  private String linkSpeed;

  @SerializedName("link_ip_address")
  private String linkIpAddress;

  @SerializedName("prefix")
  private Integer prefix;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public List<PhysicalIfIdEcEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfIdEcEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  public Integer getMinimumLinks() {
    return minimumLinks;
  }

  public void setMinimumLinks(Integer minimumLinks) {
    this.minimumLinks = minimumLinks;
  }

  public String getLinkSpeed() {
    return linkSpeed;
  }

  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  public String getLinkIpAddress() {
    return linkIpAddress;
  }

  public void setLinkIpAddress(String linkIpAddress) {
    this.linkIpAddress = linkIpAddress;
  }

  public Integer getPrefix() {
    return prefix;
  }

  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
