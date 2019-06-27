
package msf.mfcfc.node.equipments.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.core.scenario.RestRequestBase;

public class EquipmentRequest extends RestRequestBase {

  private String equipmentTypeId;

  private String format;

  public EquipmentRequest(String requestBody, String notificationAddress, String notificationPort,
      String equipmentTypeId, String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.equipmentTypeId = equipmentTypeId;
    this.format = format;
  }

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
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
