package msf.fc.rest.ec.node.interfaces.operation.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationAddInterClusterLinkOptionEcEntity;
import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationDelInterClusterLinkOptionEcEntity;


public class OperationRequestBody {

  
  @SerializedName("action")
  private String action;

  
  @SerializedName("add_inter_cluster_link_option")
  private OperationAddInterClusterLinkOptionEcEntity addInterClusterLink;

  
  @SerializedName("del_inter_cluster_link_option")
  private OperationDelInterClusterLinkOptionEcEntity delInterClusterLink;

  
  public String getAction() {
    return action;
  }

  
  public void setAction(String action) {
    this.action = action;
  }

  
  public OperationAddInterClusterLinkOptionEcEntity getAddInterClusterLink() {
    return addInterClusterLink;
  }

  
  public void setAddInterClusterLink(OperationAddInterClusterLinkOptionEcEntity addInterClusterLink) {
    this.addInterClusterLink = addInterClusterLink;
  }

  
  public OperationDelInterClusterLinkOptionEcEntity getDelInterClusterLink() {
    return delInterClusterLink;
  }

  
  public void setDelInterClusterLink(OperationDelInterClusterLinkOptionEcEntity delInterClusterLink) {
    this.delInterClusterLink = delInterClusterLink;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
