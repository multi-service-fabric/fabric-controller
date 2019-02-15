
package msf.mfcfc.traffic.traffics.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.L3CpTrafficEntity;

public class L3CpTrafficReadListResponseBody extends AbstractResponseBody {

  @SerializedName("l3cp_traffics")
  private List<L3CpTrafficEntity> l3CpTrafficList;

  public List<L3CpTrafficEntity> getCpTrafficList() {
    return l3CpTrafficList;
  }

  public void setCpTrafficList(List<L3CpTrafficEntity> l3CpTrafficList) {
    this.l3CpTrafficList = l3CpTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
