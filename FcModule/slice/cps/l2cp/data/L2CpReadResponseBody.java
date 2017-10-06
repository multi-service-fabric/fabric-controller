package msf.fc.slice.cps.l2cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.slice.cps.l2cp.data.entity.L2CpEntity;

public class L2CpReadResponseBody extends AbstractResponseBody {
  @SerializedName("l2_cp")
  private L2CpEntity l2Cp;

  public L2CpEntity getL2Cp() {
    return l2Cp;
  }

  public void setL2Cp(L2CpEntity l2Cp) {
    this.l2Cp = l2Cp;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
