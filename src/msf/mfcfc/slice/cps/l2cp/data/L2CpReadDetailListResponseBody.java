
package msf.mfcfc.slice.cps.l2cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;

public class L2CpReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("l2_cps")
  private List<L2CpEntity> l2CpList;

  public List<L2CpEntity> getL2CpList() {
    return l2CpList;
  }

  public void setL2CpList(List<L2CpEntity> l2CpList) {
    this.l2CpList = l2CpList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
