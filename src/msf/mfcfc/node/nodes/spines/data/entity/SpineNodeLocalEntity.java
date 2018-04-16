
package msf.mfcfc.node.nodes.spines.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SpineNodeLocalEntity {

  @SerializedName("breakout_ifs")
  private List<SpineNodeBreakoutIfCreateEntity> breakoutIfList;

  public List<SpineNodeBreakoutIfCreateEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  public void setBreakoutIfList(List<SpineNodeBreakoutIfCreateEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
