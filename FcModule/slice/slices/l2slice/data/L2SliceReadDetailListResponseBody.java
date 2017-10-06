package msf.fc.slice.slices.l2slice.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.slice.slices.l2slice.data.entity.L2SliceEntity;

public class L2SliceReadDetailListResponseBody extends AbstractResponseBody {
  @SerializedName("l2_slices")
  private List<L2SliceEntity> l2SliceList;

  public List<L2SliceEntity> getL2SliceList() {
    return l2SliceList;
  }

  public void setL2SliceList(List<L2SliceEntity> l2SliceList) {
    this.l2SliceList = l2SliceList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
