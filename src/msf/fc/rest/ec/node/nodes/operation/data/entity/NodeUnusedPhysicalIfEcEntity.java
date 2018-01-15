package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeUnusedPhysicalIfEcEntity {

  
  @SerializedName("physical_if_id")
  private String physicalIfId;

  
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
