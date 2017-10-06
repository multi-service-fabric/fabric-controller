package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfRemoveEcEntity {
  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("physical_ifs")
  private List<PhysicalIfIdEcEntity> physicalIfList;

  @SerializedName("minimum_links")
  private Integer minimumLinks;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
