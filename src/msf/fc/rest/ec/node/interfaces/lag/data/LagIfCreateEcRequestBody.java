package msf.fc.rest.ec.node.interfaces.lag.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfCreateEcEntity;


public class LagIfCreateEcRequestBody {

  
  @SerializedName("lag_if")
  private LagIfCreateEcEntity lagIf;

  
  public LagIfCreateEcEntity getLagIf() {
    return lagIf;
  }

  
  public void setLagIf(LagIfCreateEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
