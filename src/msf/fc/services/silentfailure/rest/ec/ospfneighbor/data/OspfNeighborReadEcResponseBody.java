
package msf.fc.services.silentfailure.rest.ec.ospfneighbor.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity.OspfNeighborListResponseEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class OspfNeighborReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("ospf_neighbors")
  private List<OspfNeighborListResponseEcEntity> ospfNeighborList;

  public List<OspfNeighborListResponseEcEntity> getOspfNeighbors() {
    return ospfNeighborList;
  }

  public void setOspfNeighbors(List<OspfNeighborListResponseEcEntity> ospfNeighborList) {
    this.ospfNeighborList = ospfNeighborList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
