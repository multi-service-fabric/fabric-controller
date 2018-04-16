
package msf.mfcfc.node.clusters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class SwClusterReadOwnerDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("clusters")
  private List<SwClusterForOwnerEntity> clusterList;

  public List<SwClusterForOwnerEntity> getClusterList() {
    return clusterList;
  }

  public void setClusterList(List<SwClusterForOwnerEntity> clusterList) {
    this.clusterList = clusterList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
