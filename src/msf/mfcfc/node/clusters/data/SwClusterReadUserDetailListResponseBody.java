package msf.mfcfc.node.clusters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class SwClusterReadUserDetailListResponseBody extends AbstractResponseBody {

  
  @SerializedName("clusters")
  private List<SwClusterForUserEntity> clusterList;

  
  public List<SwClusterForUserEntity> getClusterList() {
    return clusterList;
  }

  
  public void setClusterList(List<SwClusterForUserEntity> clusterList) {
    this.clusterList = clusterList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
