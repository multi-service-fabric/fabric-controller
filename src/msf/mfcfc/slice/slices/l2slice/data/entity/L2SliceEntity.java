
package msf.mfcfc.slice.slices.l2slice.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L2SliceEntity {

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("l2_cp_ids")
  private List<String> l2CpIdList;

  @SerializedName("remark_menu")
  private String remarkMenu;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public List<String> getL2CpIdList() {
    return l2CpIdList;
  }

  public void setL2CpIdList(List<String> l2CpIdList) {
    this.l2CpIdList = l2CpIdList;
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
