
package msf.mfcfc.node.interfaces.clusterlinkifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class ClusterLinkIfReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("cluster_link_if_ids")
  private List<ClusterLinkIfEntity> clusterLinkIfIdList;

  public List<ClusterLinkIfEntity> getClusterLinkIfIdList() {
    return clusterLinkIfIdList;
  }

  public void setClusterLinkIfIdList(List<ClusterLinkIfEntity> clusterLinkIfIdList) {
    this.clusterLinkIfIdList = clusterLinkIfIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
