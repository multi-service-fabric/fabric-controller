package msf.mfcfc.failure.logicalif.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LogicalIfUpdateLogicalIfStatusOptionEntity {

  
  @SerializedName("nodes")
  private List<LogicalIfStatusNodeEntity> nodeList;

  
  @SerializedName("ifs")
  private List<LogicalIfStatusIfEntity> ifList;

  
  public List<LogicalIfStatusNodeEntity> getNodeList() {
    return nodeList;
  }

  
  public void setNodeList(List<LogicalIfStatusNodeEntity> nodeList) {
    this.nodeList = nodeList;
  }

  
  public List<LogicalIfStatusIfEntity> getIfList() {
    return ifList;
  }

  
  public void setIfList(List<LogicalIfStatusIfEntity> ifList) {
    this.ifList = ifList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
