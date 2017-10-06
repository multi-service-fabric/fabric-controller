package msf.fc.node.clusters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class SwClusterReadOwnerResponseBody extends AbstractResponseBody {

  @SerializedName("sw_cluster")
  private SwClusterForOwnerEntity swCluster;

  public SwClusterForOwnerEntity getSwCluster() {
    return swCluster;
  }

  public void setSwCluster(SwClusterForOwnerEntity swCluster) {
    this.swCluster = swCluster;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
