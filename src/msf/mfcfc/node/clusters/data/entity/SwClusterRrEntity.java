package msf.mfcfc.node.clusters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SwClusterRrEntity {

  
  @SerializedName("peer_cluster")
  private String peerCluster;

  
  @SerializedName("accommodated_clusters")
  private List<String> accommodatedClusterList;

  
  public String getPeerCluster() {
    return peerCluster;
  }

  
  public void setPeerCluster(String peerCluster) {
    this.peerCluster = peerCluster;
  }

  
  public List<String> getAccommodatedClusterList() {
    return accommodatedClusterList;
  }

  
  public void setAccommodatedClusterList(List<String> accommodatedClusterList) {
    this.accommodatedClusterList = accommodatedClusterList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
