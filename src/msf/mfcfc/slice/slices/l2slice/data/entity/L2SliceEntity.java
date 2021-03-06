
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

  @SerializedName("irb_type")
  private String irbType;

  @SerializedName("q_in_q_enable")
  private Boolean qInQEnable;

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

  public String getIrbType() {
    return irbType;
  }

  public void setIrbType(String irbType) {
    this.irbType = irbType;
  }

  public Boolean getQInQEnable() {
    return qInQEnable;
  }

  public void setQInQEnable(Boolean qInQEnable) {
    this.qInQEnable = qInQEnable;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
