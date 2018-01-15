
package msf.mfcfc.slice.cps.l3cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;


public class L3CpReadResponseBody extends AbstractResponseBody {

  
  @SerializedName("l3_cp")
  private L3CpEntity l3Cp;

  
  public L3CpEntity getL3Cp() {
    return l3Cp;
  }

  
  public void setL3Cp(L3CpEntity l3Cp) {
    this.l3Cp = l3Cp;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
