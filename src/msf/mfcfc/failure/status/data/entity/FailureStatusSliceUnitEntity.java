package msf.mfcfc.failure.status.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class FailureStatusSliceUnitEntity {

  
  @SerializedName("slices")
  private List<FailureStatusSliceFailureEntity> sliceList;

  
  @SerializedName("reachable_statuses")
  private List<FailureStatusReachableStatusFailureEntity> reachableStatusList;

  
  public List<FailureStatusSliceFailureEntity> getSliceList() {
    return sliceList;
  }

  
  public void setSliceList(List<FailureStatusSliceFailureEntity> sliceList) {
    this.sliceList = sliceList;
  }

  
  public List<FailureStatusReachableStatusFailureEntity> getReachableStatusList() {
    return reachableStatusList;
  }

  
  public void setReachableStatusList(List<FailureStatusReachableStatusFailureEntity> reachableStatusList) {
    this.reachableStatusList = reachableStatusList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
