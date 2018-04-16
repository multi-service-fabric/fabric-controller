
package msf.mfcfc.slice.cps.l3cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.core.scenario.RestRequestBase;

public class L3CpRequest extends RestRequestBase {

  public L3CpRequest(String requestBody, String notificationAddress, String notificationPort, String sliceId,
      String cpId, String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.sliceId = sliceId;
    this.cpId = cpId;
    this.format = format;
  }

  private String sliceType = SliceType.L3_SLICE.getMessage();

  private String sliceId;

  private String cpId;

  private String format;

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

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public SliceType getSliceTypeEnum() {
    return SliceType.getEnumFromMessage(sliceType);
  }

  public void setSliceTypeEnum(SliceType sliceType) {
    this.sliceType = sliceType.getMessage();
  }

  public RestFormatOption getFormatEnum() {
    return RestFormatOption.getEnumFromMessage(format);
  }

  public void setFormatEnum(RestFormatOption format) {
    this.format = format.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
