package msf.mfcfc.node.interfaces.clusterlinkifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class ClusterLinkIfReadListResponseBody extends AbstractResponseBody {
  
  @SerializedName("cluster_link_if_ids")
  private List<String> clusterLinkIfIdList;

  
  public List<String> getClusterLinkIfIdList() {
    return clusterLinkIfIdList;
  }

  
  public void setClusterLinkIfIdList(List<String> clusterLinkIfIdList) {
    this.clusterLinkIfIdList = clusterLinkIfIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
