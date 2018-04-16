
package msf.mfcfc.slice.slices.l2slice.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class L2SliceUpdateAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("updated_cp_ids")
  private List<String> updatedCpIdList;

  public List<String> getUpdatedCpIdList() {
    return updatedCpIdList;
  }

  public void setUpdatedCpIdList(List<String> updatedCpIdList) {
    this.updatedCpIdList = updatedCpIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
