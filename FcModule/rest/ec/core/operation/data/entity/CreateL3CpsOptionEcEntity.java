package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.PlaneBelongsTo;

public class CreateL3CpsOptionEcEntity {
  @SerializedName("cps")
  private List<CreateL3CpEcEntity> l3CpList;
  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("plane")
  private Integer plane;

  public List<CreateL3CpEcEntity> getL3CpList() {
    return l3CpList;
  }

  public void setL3CpList(List<CreateL3CpEcEntity> l3CpList) {
    this.l3CpList = l3CpList;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public PlaneBelongsTo getPlaneEnum() {
    return PlaneBelongsTo.getEnumFromMessage(plane);
  }

  public void setPlaneEnum(PlaneBelongsTo plane) {
    this.plane = plane.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
