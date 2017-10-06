package msf.fc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SliceType;
import msf.fc.core.scenario.RestRequestBase;

public class L2SliceRequest extends RestRequestBase {
  private String sliceType = SliceType.L2_SLICE.getMessage();
  private String sliceId;
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
