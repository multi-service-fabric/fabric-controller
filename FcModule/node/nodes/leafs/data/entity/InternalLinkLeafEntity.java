package msf.fc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalLinkLeafEntity {

  @SerializedName("physical_links")
  private List<PhysicalLinkLeafEntity> physicalLinkList;

  public List<PhysicalLinkLeafEntity> getPhysicalLinkList() {
    return physicalLinkList;
  }

  public void setPhysicalLinkList(List<PhysicalLinkLeafEntity> physicalLinkList) {
    this.physicalLinkList = physicalLinkList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
