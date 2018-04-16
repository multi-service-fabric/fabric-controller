
package msf.mfcfc.traffic.traffics.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class IfTrafficClusterUnitEntity {

  @SerializedName("clusters")
  private List<IfTrafficClusterEntity> clusterList;

  public List<IfTrafficClusterEntity> getClusterList() {
    return clusterList;
  }

  public void setClusterList(List<IfTrafficClusterEntity> clusterList) {
    this.clusterList = clusterList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
