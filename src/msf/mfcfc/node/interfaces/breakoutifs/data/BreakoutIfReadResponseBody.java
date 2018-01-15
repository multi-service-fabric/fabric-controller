package msf.mfcfc.node.interfaces.breakoutifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class BreakoutIfReadResponseBody extends AbstractResponseBody {

  
  @SerializedName("breakout_if")
  private BreakoutIfEntity breakoutIf;

  
  public BreakoutIfEntity getBreakoutIf() {
    return breakoutIf;
  }

  
  public void setBreakoutIf(BreakoutIfEntity breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
