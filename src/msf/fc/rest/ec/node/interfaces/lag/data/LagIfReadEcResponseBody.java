
package msf.fc.rest.ec.node.interfaces.lag.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class LagIfReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("lag_if")
  private LagIfEcEntity lagIf;

  public LagIfEcEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(LagIfEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
