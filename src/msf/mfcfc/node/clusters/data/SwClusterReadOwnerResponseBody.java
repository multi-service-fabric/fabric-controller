
package msf.mfcfc.node.clusters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class SwClusterReadOwnerResponseBody extends AbstractResponseBody {

  @SerializedName("cluster")
  private SwClusterForOwnerEntity cluster;

  public SwClusterForOwnerEntity getCluster() {
    return cluster;
  }

  public void setCluster(SwClusterForOwnerEntity cluster) {
    this.cluster = cluster;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
