
package msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OspfNeighborListEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("ospf_neighbor_ifs")
  private List<OspfNeighborIfListEcEntity> ospfNeighborIfList;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public List<OspfNeighborIfListEcEntity> getOspfNeighborIfList() {
    return ospfNeighborIfList;
  }

  public void setOspfNeighborIfList(List<OspfNeighborIfListEcEntity> ospfNeighborIfList) {
    this.ospfNeighborIfList = ospfNeighborIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
