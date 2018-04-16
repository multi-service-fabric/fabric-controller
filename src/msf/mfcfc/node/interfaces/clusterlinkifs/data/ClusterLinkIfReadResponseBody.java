
package msf.mfcfc.node.interfaces.clusterlinkifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class ClusterLinkIfReadResponseBody extends AbstractResponseBody {

  @SerializedName("cluster_link_if")
  private ClusterLinkIfEntity clusterLinkIf;

  public ClusterLinkIfEntity getClusterLinkIf() {
    return clusterLinkIf;
  }

  public void setClusterLinkIf(ClusterLinkIfEntity clusterLinkIf) {
    this.clusterLinkIf = clusterLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
