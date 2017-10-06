package msf.fc.rest.ec.node.interfaces.lag.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.ReadLagIfEcEntity;

public class LagIfReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("lag_ifs")
  private List<ReadLagIfEcEntity> lagIf;

  public List<ReadLagIfEcEntity> getLagIf() {
    return lagIf;
  }

  public void setLagIf(List<ReadLagIfEcEntity> lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
