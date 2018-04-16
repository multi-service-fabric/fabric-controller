
package msf.mfcfc.traffic.traffics.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.SliceType;

public class IfTrafficSliceEntity {

  @SerializedName("slice_type")
  private String sliceType;

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("cp_ids")
  private List<String> cpIdList;

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

  public SliceType getSliceTypeEnum() {
    return SliceType.getEnumFromMessage(sliceType);
  }

  public void setSliceTypeEnum(SliceType sliceType) {
    this.sliceType = sliceType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
