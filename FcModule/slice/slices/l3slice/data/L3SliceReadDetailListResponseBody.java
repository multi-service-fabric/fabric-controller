package msf.fc.slice.slices.l3slice.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.slice.slices.l3slice.data.entity.L3SliceEntity;

public class L3SliceReadDetailListResponseBody extends AbstractResponseBody {
  @SerializedName("l3_slices")
  private List<L3SliceEntity> l3SliceList;

  public List<L3SliceEntity> getL3SliceList() {
    return l3SliceList;
  }

  public void setL3SliceList(List<L3SliceEntity> l3SliceList) {
    this.l3SliceList = l3SliceList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
