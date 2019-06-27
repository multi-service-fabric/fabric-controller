
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeEntity;

public class NodeOsUpgradeAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("nodes")
  private List<NodeOsUpgradeEntity> nodeList;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public List<NodeOsUpgradeEntity> getNodeList() {
    return nodeList;
  }

  public void setNodeList(List<NodeOsUpgradeEntity> nodeList) {
    this.nodeList = nodeList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
