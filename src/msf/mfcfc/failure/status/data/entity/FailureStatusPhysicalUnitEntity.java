package msf.mfcfc.failure.status.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class FailureStatusPhysicalUnitEntity {

  
  @SerializedName("nodes")
  private List<FailureStatusNodeFailureEntity> nodeList;

  
  @SerializedName("ifs")
  private List<FailureStatusIfFailureEntity> ifList;

  
  public List<FailureStatusNodeFailureEntity> getNodeList() {
    return nodeList;
  }

  
  public void setNodeList(List<FailureStatusNodeFailureEntity> nodeList) {
    this.nodeList = nodeList;
  }

  
  public List<FailureStatusIfFailureEntity> getIfList() {
    return ifList;
  }

  
  public void setIfList(List<FailureStatusIfFailureEntity> ifList) {
    this.ifList = ifList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
