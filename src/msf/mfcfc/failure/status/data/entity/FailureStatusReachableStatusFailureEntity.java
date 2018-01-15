package msf.mfcfc.failure.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class FailureStatusReachableStatusFailureEntity {

  
  @SerializedName("type")
  private String type;

  
  @SerializedName("id")
  private String id;

  
  @SerializedName("opposite_type")
  private String oppositeType;

  
  @SerializedName("opposite_id")
  private String oppositeId;

  
  @SerializedName("reachable_status")
  private String reachableStatus;

  
  public String getType() {
    return type;
  }

  
  public void setType(String type) {
    this.type = type;
  }

  
  public String getId() {
    return id;
  }

  
  public void setId(String id) {
    this.id = id;
  }

  
  public String getOppositeType() {
    return oppositeType;
  }

  
  public void setOppositeType(String oppositeType) {
    this.oppositeType = oppositeType;
  }

  
  public String getOppositeId() {
    return oppositeId;
  }

  
  public void setOppositeId(String oppositeId) {
    this.oppositeId = oppositeId;
  }

  
  public String getReachableStatus() {
    return reachableStatus;
  }

  
  public void setReachableStatus(String reachableStatus) {
    this.reachableStatus = reachableStatus;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
