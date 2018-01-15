
package msf.mfcfc.slice.cps.l2cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class L2CpReadListResponseBody extends AbstractResponseBody {

  
  @SerializedName("l2_cp_ids")
  private List<String> l2CpIdList;

  
  public List<String> getL2CpIdList() {
    return l2CpIdList;
  }

  
  public void setL2CpIdList(List<String> l2CpIdList) {
    this.l2CpIdList = l2CpIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
