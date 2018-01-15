package msf.mfcfc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.LeafType;


public class LeafNodeTypeOptionEntity {

  
  @SerializedName("leaf_type")
  private String leafType;

  
  public String getLeafType() {
    return leafType;
  }

  
  public void setLeafType(String leafType) {
    this.leafType = leafType;
  }

  
  public LeafType getLeafTypeEnum() {
    return LeafType.getEnumFromMessage(leafType);
  }

  
  public void setLeafTypeEnum(LeafType leafType) {
    this.leafType = leafType.getMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
