
package msf.mfcfc.slice.cps.l3cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L3CpUpdateOptionEntity {

  @SerializedName("qos_update_option")
  private L3CpQosCreateEntity qosUpdateOption;

  public L3CpQosCreateEntity getQosUpdateOption() {
    return qosUpdateOption;
  }

  public void setQosUpdateOption(L3CpQosCreateEntity qosUpdateOption) {
    this.qosUpdateOption = qosUpdateOption;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
