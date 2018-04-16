
package msf.mfcfc.node.interfaces.breakoutifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class BreakoutIfCreateAsyncResponseBody extends AbstractResponseBody {

  @SerializedName("breakout_if_ids")
  private List<String> breakoutIfIdList;

  public List<String> getBreakoutIfIdList() {
    return breakoutIfIdList;
  }

  public void setBreakoutIfIdList(List<String> breakoutIfIdList) {
    this.breakoutIfIdList = breakoutIfIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
