
package msf.fc.rest.ec.node.interfaces.lag.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfUpdateEcEntity {

  @SerializedName("link_type")
  private String linkType;

  @SerializedName("physical_ifs")
  private List<LagIfPhysicalIfUpdateEcEntity> physicalIfsList;

  public String getLinkType() {
    return linkType;
  }

  public void setLinkType(String linkType) {
    this.linkType = linkType;
  }

  public List<LagIfPhysicalIfUpdateEcEntity> getPhysicalIfsList() {
    return physicalIfsList;
  }

  public void setPhysicalIfsList(List<LagIfPhysicalIfUpdateEcEntity> physicalIfsList) {
    this.physicalIfsList = physicalIfsList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
