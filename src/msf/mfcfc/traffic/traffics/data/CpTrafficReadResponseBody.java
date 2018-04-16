
package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficEntity;

public class CpTrafficReadResponseBody extends AbstractResponseBody {

  @SerializedName("cp_traffic")
  private CpTrafficEntity cpTraffic;

  public CpTrafficEntity getCpTraffic() {
    return cpTraffic;
  }

  public void setCpTraffic(CpTrafficEntity cpTraffic) {
    this.cpTraffic = cpTraffic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
