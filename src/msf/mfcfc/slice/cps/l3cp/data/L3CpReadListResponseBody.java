
package msf.mfcfc.slice.cps.l3cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class L3CpReadListResponseBody extends AbstractResponseBody {

  @SerializedName("l3_cp_ids")
  private List<String> l3CpIdList;

  public List<String> getL3CpIdList() {
    return l3CpIdList;
  }

  public void setL3CpIdList(List<String> l3CpIdList) {
    this.l3CpIdList = l3CpIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
