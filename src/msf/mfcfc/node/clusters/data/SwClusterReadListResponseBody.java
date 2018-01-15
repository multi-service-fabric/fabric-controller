package msf.mfcfc.node.clusters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class SwClusterReadListResponseBody extends AbstractResponseBody {

  
  @SerializedName("cluster_ids")
  private List<String> clusterIdList;

  
  public List<String> getClusterIdList() {
    return clusterIdList;
  }

  
  public void setClusterIdList(List<String> clusterIdList) {
    this.clusterIdList = clusterIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
