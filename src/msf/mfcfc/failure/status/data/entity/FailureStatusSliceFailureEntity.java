package msf.mfcfc.failure.status.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class FailureStatusSliceFailureEntity {

  
  @SerializedName("slice_id")
  private String sliceId;

  
  @SerializedName("slice_type")
  private String sliceType;

  
  @SerializedName("cp_ids")
  private List<String> cpIdList;

  
  @SerializedName("failure_status")
  private String failureStatus;

  
  public String getSliceId() {
    return sliceId;
  }

  
  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  
  public String getSliceType() {
    return sliceType;
  }

  
  public void setSliceType(String sliceType) {
    this.sliceType = sliceType;
  }

  
  public List<String> getCpIdList() {
    return cpIdList;
  }

  
  public void setCpIdList(List<String> cpIdList) {
    this.cpIdList = cpIdList;
  }

  
  public String getFailureStatus() {
    return failureStatus;
  }

  
  public void setFailureStatus(String failureStatus) {
    this.failureStatus = failureStatus;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
