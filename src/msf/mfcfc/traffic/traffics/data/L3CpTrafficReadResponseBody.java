
package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.L3CpTrafficEntity;

public class L3CpTrafficReadResponseBody extends AbstractResponseBody {

  @SerializedName("l3cp_traffic")
  private L3CpTrafficEntity l3CpTraffic;

  public L3CpTrafficEntity getCpTraffic() {
    return l3CpTraffic;
  }

  public void setCpTraffic(L3CpTrafficEntity l3CpTraffic) {
    this.l3CpTraffic = l3CpTraffic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
