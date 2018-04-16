
package msf.mfcfc.node.nodes.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeCreateNodeIfEntity {

  @SerializedName("breakout_base_ifs")
  private List<NodeBreakoutBaseIfEntity> breakoutBaseIfList;

  @SerializedName("internal_link_ifs")
  private List<NodeInternalLinkIfEntity> internalLinkIfList;

  @SerializedName("unused_physical_ifs")
  private List<NodeUnusedPhysicalEntity> unusedPhysicalIfList;

  public List<NodeBreakoutBaseIfEntity> getBreakoutBaseIfList() {
    return breakoutBaseIfList;
  }

  public void setBreakoutBaseIfList(List<NodeBreakoutBaseIfEntity> breakoutBaseIfList) {
    this.breakoutBaseIfList = breakoutBaseIfList;
  }

  public List<NodeInternalLinkIfEntity> getInternalLinkIfList() {
    return internalLinkIfList;
  }

  public void setInternalLinkIfList(List<NodeInternalLinkIfEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  public List<NodeUnusedPhysicalEntity> getUnusedPhysicalIfList() {
    return unusedPhysicalIfList;
  }

  public void setUnusedPhysicalIfList(List<NodeUnusedPhysicalEntity> unusedPhysicalIfList) {
    this.unusedPhysicalIfList = unusedPhysicalIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
