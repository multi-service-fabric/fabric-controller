package msf.fc.rest.ec.node.nodes.spine.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class DelNodeOptionEcEntity {

  @SerializedName("opposite_nodes")
  private List<OppositeNodeRemoveEcEntity> oppositeNodeList;

  public List<OppositeNodeRemoveEcEntity> getOppositeNodeList() {
    return oppositeNodeList;
  }

  public void setOppositeNodeList(List<OppositeNodeRemoveEcEntity> oppositeNodeList) {
    this.oppositeNodeList = oppositeNodeList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
