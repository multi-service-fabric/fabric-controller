package msf.fc.rest.ec.node.interfaces.lag.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.lag.data.entity.CreateLagIfEcEntity;

public class LagIfCreateEcRequestBody {

  @SerializedName("lag_if")
  private CreateLagIfEcEntity lagIf;

  public CreateLagIfEcEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(CreateLagIfEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
