
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailureStatusSliceClusterLinkFailureEntity implements Serializable {

  @SerializedName("reachable_statuses")
  private List<FailureStatusSliceClusterLinkReachableStatusEntity> reachableStatusList;

  public List<FailureStatusSliceClusterLinkReachableStatusEntity> getReachableStatusList() {
    return reachableStatusList;
  }

  public void setReachableStatusList(List<FailureStatusSliceClusterLinkReachableStatusEntity> reachableStatusList) {
    this.reachableStatusList = reachableStatusList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
