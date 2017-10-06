package msf.fc.rest.ec.node.interfaces.lag.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.ReadLagIfEcEntity;

public class LagIfReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("lag_if")
  private ReadLagIfEcEntity lagIf;

  public ReadLagIfEcEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(ReadLagIfEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
