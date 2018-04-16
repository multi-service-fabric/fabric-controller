
package msf.fc.rest.ec.node.interfaces.breakout.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class BreakoutIfReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("breakout_if")
  private BreakoutIfEcEntity breakoutIf;

  public BreakoutIfEcEntity getBreakoutIf() {
    return breakoutIf;
  }

  public void setBreakoutIf(BreakoutIfEcEntity breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
