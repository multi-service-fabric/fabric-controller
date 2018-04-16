
package msf.mfcfc.slice.slices.l3slice.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L3SliceEntity {

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("plane")
  private Integer plane;

  @SerializedName("l3_cp_ids")
  private List<String> l3CpIdList;

  @SerializedName("remark_menu")
  private String remarkMenu;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public List<String> getL3CpIdList() {
    return l3CpIdList;
  }

  public void setL3CpIdList(List<String> l3CpIdList) {
    this.l3CpIdList = l3CpIdList;
  }

  public String getRemarkMenu() {
    return remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
