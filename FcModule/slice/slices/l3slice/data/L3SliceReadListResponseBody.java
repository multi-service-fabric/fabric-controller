package msf.fc.slice.slices.l3slice.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class L3SliceReadListResponseBody extends AbstractResponseBody {
  @SerializedName("l3_slice_ids")
  private List<String> l3SliceIdList;

  public List<String> getL3SliceIdList() {
    return l3SliceIdList;
  }

  public void setL3SliceIdList(List<String> l3SliceIdList) {
    this.l3SliceIdList = l3SliceIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
