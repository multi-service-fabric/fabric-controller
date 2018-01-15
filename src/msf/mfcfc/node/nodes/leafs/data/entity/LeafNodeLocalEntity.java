package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LeafNodeLocalEntity {

  
  @SerializedName("breakout_ifs")
  private List<LeafNodeBreakoutIfCreateEntity> breakoutIfList;

  
  public List<LeafNodeBreakoutIfCreateEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  
  public void setBreakoutIfList(List<LeafNodeBreakoutIfCreateEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
