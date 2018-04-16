
package msf.fc.rest.ec.node.interfaces.breakout.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class BreakoutIfReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("breakout_ifs")
  private List<BreakoutIfEcEntity> breakoutIfList;

  public List<BreakoutIfEcEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  public void setBreakoutIfList(List<BreakoutIfEcEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
