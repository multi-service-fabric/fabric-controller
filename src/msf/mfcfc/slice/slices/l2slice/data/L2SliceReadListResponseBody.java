
package msf.mfcfc.slice.slices.l2slice.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class L2SliceReadListResponseBody extends AbstractResponseBody {

  @SerializedName("l2_slice_ids")
  private List<String> l2SliceIdList;

  public List<String> getL2SliceIdList() {
    return l2SliceIdList;
  }

  public void setL2SliceIdList(List<String> l2SliceIdList) {
    this.l2SliceIdList = l2SliceIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
