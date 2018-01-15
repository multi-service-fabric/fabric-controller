package msf.mfcfc.failure.status.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class FailureStatusClusterUnitEntity {

  
  @SerializedName("clusters")
  private List<FailureStatusClusterFailureEntity> clusterList;

  
  public List<FailureStatusClusterFailureEntity> getClusterList() {
    return clusterList;
  }

  
  public void setClusterList(List<FailureStatusClusterFailureEntity> clusterList) {
    this.clusterList = clusterList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
