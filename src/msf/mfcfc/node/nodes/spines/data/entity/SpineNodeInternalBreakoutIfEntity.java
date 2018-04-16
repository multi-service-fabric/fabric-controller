
package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SpineNodeInternalBreakoutIfEntity {

  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
