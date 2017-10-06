package msf.fc.node.clusters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class SwClusterReadUserResponseBody extends AbstractResponseBody {

  @SerializedName("sw_cluster")
  private SwClusterForUserEntity swCluster;

  public SwClusterForUserEntity getSwCluster() {
    return swCluster;
  }

  public void setSwCluster(SwClusterForUserEntity swCluster) {
    this.swCluster = swCluster;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
