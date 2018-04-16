
package msf.mfcfc.node.interfaces.lagifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class LagIfCreateAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("lag_if_id")
  private String lagIfId;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
