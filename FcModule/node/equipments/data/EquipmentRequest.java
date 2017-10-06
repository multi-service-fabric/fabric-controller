package msf.fc.node.equipments.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.RestFormatOption;
import msf.fc.core.scenario.RestRequestBase;

public class EquipmentRequest extends RestRequestBase {

  private String clusterId;

  private String equipmentTypeId;

  private String format;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
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
