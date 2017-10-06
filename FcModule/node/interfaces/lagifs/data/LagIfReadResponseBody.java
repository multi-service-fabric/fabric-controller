package msf.fc.node.interfaces.lagifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class LagIfReadResponseBody extends AbstractResponseBody {

  @SerializedName("lag_if")
  private LagIfEntity lagIf;

  public LagIfEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(LagIfEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
