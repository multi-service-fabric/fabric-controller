
package msf.mfcfc.slice.cps.l2cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class L2CpCreateDeleteAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("cp_ids")
  private List<String> cpIdList;

  public List<String> getCpIdList() {
    return cpIdList;
  }

  public void setCpIdList(List<String> cpIdList) {
    this.cpIdList = cpIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
