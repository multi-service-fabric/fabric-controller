
package msf.fc.rest.ec.node.interfaces.lag.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfUpdateEcEntity;

public class LagIfUpdateEcRequestBody {

  @SerializedName("action")
  private String action;

  @SerializedName("lag_if")
  private LagIfUpdateEcEntity lagIf;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public LagIfUpdateEcEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(LagIfUpdateEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
