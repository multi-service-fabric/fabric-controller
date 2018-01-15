package msf.fc.rest.ec.node.nodes.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeCreateNodeIfEcEntity {

  
  @SerializedName("breakout_base_ifs")
  private List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfList;

  
  @SerializedName("internal_link_ifs")
  private List<NodeInternalLinkCreateEcEntity> internalLinkList;

  
  @SerializedName("unused_physical_ifs")
  private List<NodeUnusedPhysicalIfEcEntity> unusedPhysicalIfList;

  
  public List<NodeBreakoutBaseIfEcEntity> getBreakoutBaseIfList() {
    return breakoutBaseIfList;
  }

  
  public void setBreakoutBaseIfList(List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfList) {
    this.breakoutBaseIfList = breakoutBaseIfList;
  }

  
  public List<NodeInternalLinkCreateEcEntity> getInternalLinkList() {
    return internalLinkList;
  }

  
  public void setInternalLinkList(List<NodeInternalLinkCreateEcEntity> internalLinkList) {
    this.internalLinkList = internalLinkList;
  }

  
  public List<NodeUnusedPhysicalIfEcEntity> getUnusedPhysicalIfList() {
    return unusedPhysicalIfList;
  }

  
  public void setUnusedPhysicalIfList(List<NodeUnusedPhysicalIfEcEntity> unusedPhysicalIfList) {
    this.unusedPhysicalIfList = unusedPhysicalIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
