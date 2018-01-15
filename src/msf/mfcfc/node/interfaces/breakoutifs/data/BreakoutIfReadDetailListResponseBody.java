package msf.mfcfc.node.interfaces.breakoutifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class BreakoutIfReadDetailListResponseBody extends AbstractResponseBody {

  
  @SerializedName("breakout_ifs")
  private List<BreakoutIfEntity> breakoutIfList;

  
  public List<BreakoutIfEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  
  public void setBreakoutIfList(List<BreakoutIfEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
