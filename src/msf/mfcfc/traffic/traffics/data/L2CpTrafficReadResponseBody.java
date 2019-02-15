
package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.L2CpTrafficEntity;

public class L2CpTrafficReadResponseBody extends AbstractResponseBody {

  @SerializedName("l2cp_traffic")
  private L2CpTrafficEntity l2CpTraffic;

  public L2CpTrafficEntity getCpTraffic() {
    return l2CpTraffic;
  }

  public void setCpTraffic(L2CpTrafficEntity l2CpTraffic) {
    this.l2CpTraffic = l2CpTraffic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
