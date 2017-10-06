package msf.fc.slice.slices.l3slice.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class L3SliceUpdateAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("updated_cps")
  private List<String> updatedCpList;

  public List<String> getUpdatedCpList() {
    return updatedCpList;
  }

  public void setUpdatedCpList(List<String> updatedCpList) {
    this.updatedCpList = updatedCpList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
