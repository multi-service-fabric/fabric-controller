
package msf.mfcfc.traffic.traffics.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.L2CpTrafficEntity;

public class L2CpTrafficReadListResponseBody extends AbstractResponseBody {

  @SerializedName("l2cp_traffics")
  private List<L2CpTrafficEntity> l2CpTrafficList;

  public List<L2CpTrafficEntity> getCpTrafficList() {
    return l2CpTrafficList;
  }

  public void setCpTrafficList(List<L2CpTrafficEntity> l2CpTrafficList) {
    this.l2CpTrafficList = l2CpTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
