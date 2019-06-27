
package msf.fc.services.silentfailure.rest.ec.ospfneighbor.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity.OspfNeighborListEcEntity;

public class OspfNeighborReadEcRequestBody {

  @SerializedName("ospf_neighbors")
  private List<OspfNeighborListEcEntity> ospfNeighborList;

  public List<OspfNeighborListEcEntity> getOspfNeighbors() {
    return ospfNeighborList;
  }

  public void setOspfNeighbors(List<OspfNeighborListEcEntity> ospfNeighborList) {
    this.ospfNeighborList = ospfNeighborList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
