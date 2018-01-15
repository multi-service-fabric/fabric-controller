package msf.fc.rest.ec.node.interfaces.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class OperationAddInterClusterLinkOptionEcEntity {

  
  @SerializedName("target_if")
  private OperationTargetIfEcEntity targetIf;

  
  @SerializedName("cluster_link")
  private OperationClusterLinkEcEntity clusterLink;

  
  public OperationTargetIfEcEntity getTargetIf() {
    return targetIf;
  }

  
  public void setTargetIf(OperationTargetIfEcEntity targetIf) {
    this.targetIf = targetIf;
  }

  
  public OperationClusterLinkEcEntity getClusterLink() {
    return clusterLink;
  }

  
  public void setClusterLink(OperationClusterLinkEcEntity clusterLink) {
    this.clusterLink = clusterLink;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
