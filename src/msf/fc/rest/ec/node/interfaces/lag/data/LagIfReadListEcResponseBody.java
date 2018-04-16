
package msf.fc.rest.ec.node.interfaces.lag.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class LagIfReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("lag_ifs")
  private List<LagIfEcEntity> lagIfList;

  public List<LagIfEcEntity> getLagIf() {
    return lagIfList;
  }

  public void setLagIf(List<LagIfEcEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
