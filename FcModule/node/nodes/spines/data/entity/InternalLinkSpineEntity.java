package msf.fc.node.nodes.spines.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalLinkSpineEntity {

  @SerializedName("physical_links")
  private List<PhysicalLinkSpineEntity> physicalLinkList;

  public List<PhysicalLinkSpineEntity> getPhysicalLinkList() {
    return physicalLinkList;
  }

  public void setPhysicalLinkList(List<PhysicalLinkSpineEntity> physicalLinkList) {
    this.physicalLinkList = physicalLinkList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
