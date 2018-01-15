
package msf.mfcfc.slice.cps.l2cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class L2CpUpdateOptionEntity {

  
  @SerializedName("qos_update_option")
  private L2CpQosCreateEntity qosUpdateOption;

  
  public L2CpQosCreateEntity getQosUpdateOption() {
    return qosUpdateOption;
  }

  
  public void setQosUpdateOption(L2CpQosCreateEntity qosUpdateOption) {
    this.qosUpdateOption = qosUpdateOption;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
