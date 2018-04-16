
package msf.mfcfc.node.clusters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class SwClusterReadUserResponseBody extends AbstractResponseBody {

  @SerializedName("cluster")
  private SwClusterForUserEntity cluster;

  public SwClusterForUserEntity getCluster() {
    return cluster;
  }

  public void setCluster(SwClusterForUserEntity cluster) {
    this.cluster = cluster;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
