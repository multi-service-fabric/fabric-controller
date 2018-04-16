
package msf.mfcfc.node.nodes.spines.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SpineNodeInternalLinkEntity {

  @SerializedName("physical_links")
  private List<SpineNodePhysicalLinkEntity> physicalLinkList;

  @SerializedName("lag_links")
  private List<SpineNodeLagLinkEntity> lagLinkList;

  public List<SpineNodePhysicalLinkEntity> getPhysicalLinkList() {
    return physicalLinkList;
  }

  public void setPhysicalLinkList(List<SpineNodePhysicalLinkEntity> physicalLinkList) {
    this.physicalLinkList = physicalLinkList;
  }

  public List<SpineNodeLagLinkEntity> getLagLinkList() {
    return lagLinkList;
  }

  public void setLagLinkList(List<SpineNodeLagLinkEntity> lagLinkList) {
    this.lagLinkList = lagLinkList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
