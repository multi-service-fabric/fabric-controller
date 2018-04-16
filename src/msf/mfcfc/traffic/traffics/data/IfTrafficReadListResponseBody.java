
package msf.mfcfc.traffic.traffics.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficEntity;

public class IfTrafficReadListResponseBody extends AbstractResponseBody {

  @SerializedName("if_traffics")
  private List<IfTrafficEntity> ifTrafficList;

  public List<IfTrafficEntity> getIfTrafficList() {
    return ifTrafficList;
  }

  public void setIfTrafficList(List<IfTrafficEntity> ifTrafficList) {
    this.ifTrafficList = ifTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
