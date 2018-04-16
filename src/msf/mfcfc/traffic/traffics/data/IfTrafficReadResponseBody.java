
package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficEntity;

public class IfTrafficReadResponseBody extends AbstractResponseBody {

  @SerializedName("if_traffic")
  private IfTrafficEntity ifTraffic;

  public IfTrafficEntity getIfTraffic() {
    return ifTraffic;
  }

  public void setIfTraffic(IfTrafficEntity ifTraffic) {
    this.ifTraffic = ifTraffic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
