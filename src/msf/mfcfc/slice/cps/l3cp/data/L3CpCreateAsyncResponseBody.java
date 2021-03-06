
package msf.mfcfc.slice.cps.l3cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class L3CpCreateAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("cp_id")
  private String cpId;

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
