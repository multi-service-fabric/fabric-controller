package msf.mfcfc.node.interfaces.clusterlinkifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class ClusterLinkIfCreaterAsyncResponseBody extends AbstractResponseBody {
  
  @SerializedName("cluster_link_if_id")
  private String clusterLinkIfId;

  
  public String getClusterLinkIfId() {
    return clusterLinkIfId;
  }

  
  public void setClusterLinkIfId(String clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
