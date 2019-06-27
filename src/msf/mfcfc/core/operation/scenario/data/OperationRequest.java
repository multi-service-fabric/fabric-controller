
package msf.mfcfc.core.operation.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.core.scenario.RestRequestBase;

public class OperationRequest extends RestRequestBase {

  private String operationId;

  private String format;

  public OperationRequest(String requestBody, String notificationAddress, String notificationPort, String operationId,
      String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.operationId = operationId;
    this.format = format;
  }

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
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
