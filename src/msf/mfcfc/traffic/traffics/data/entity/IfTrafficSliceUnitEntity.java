
package msf.mfcfc.traffic.traffics.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class IfTrafficSliceUnitEntity {

  @SerializedName("slices")
  private List<IfTrafficSliceEntity> sliceList;

  public List<IfTrafficSliceEntity> getSliceList() {
    return sliceList;
  }

  public void setSliceList(List<IfTrafficSliceEntity> sliceList) {
    this.sliceList = sliceList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
