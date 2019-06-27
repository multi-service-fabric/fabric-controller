
package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.core.scenario.RestRequestBase;

public class L3CpTrafficRequest extends RestRequestBase {

  private String sliceType = SliceType.L3_SLICE.getMessage();

  private String sliceId;

  private String cpId;

  public L3CpTrafficRequest(String requestBody, String notificationAddress, String notificationPort, String sliceType,
      String sliceId, String cpId) {
    super(requestBody, notificationAddress, notificationPort);
    this.sliceType = sliceType;
    this.sliceId = sliceId;
    this.cpId = cpId;
  }

  public String getSliceType() {
    return sliceType;
  }

  public void setSliceType(String sliceType) {
    this.sliceType = sliceType;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public SliceType getSliceTypeEnum() {
    return SliceType.getEnumFromMessage(sliceType);
  }

  public void setSliceTypeEnum(SliceType sliceType) {
    this.sliceType = sliceType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
