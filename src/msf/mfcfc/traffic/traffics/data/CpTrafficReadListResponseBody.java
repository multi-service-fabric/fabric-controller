
package msf.mfcfc.traffic.traffics.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficEntity;

public class CpTrafficReadListResponseBody extends AbstractResponseBody {

  @SerializedName("cp_traffics")
  private List<CpTrafficEntity> cpTrafficList;

  public List<CpTrafficEntity> getCpTrafficList() {
    return cpTrafficList;
  }

  public void setCpTrafficList(List<CpTrafficEntity> cpTrafficList) {
    this.cpTrafficList = cpTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
