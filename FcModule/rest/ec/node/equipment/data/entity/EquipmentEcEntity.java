package msf.fc.rest.ec.node.equipment.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentEcEntity {

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("lag_prefix")
  private String lagPrefix;

  @SerializedName("unit_connector")
  private String unitConnector;

  @SerializedName("if_name_oid")
  private String ifNameOid;

  @SerializedName("snmptrap_if_name_oid")
  private String snmptrapIfNameOid;

  @SerializedName("max_repetitions")
  private Integer maxRepetitions;

  @SerializedName("if_name_rules")
  private List<IfNameRuleEcEntity> ifNameRuleList;

  @SerializedName("equipment_ifs")
  private List<EquipmentIfEcEntity> equipmentIfList;

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getLagPrefix() {
    return lagPrefix;
  }

  public void setLagPrefix(String lagPrefix) {
    this.lagPrefix = lagPrefix;
  }

  public String getUnitConnector() {
    return unitConnector;
  }

  public void setUnitConnector(String unitConnector) {
    this.unitConnector = unitConnector;
  }

  public String getIfNameOid() {
    return ifNameOid;
  }

  public void setIfNameOid(String ifNameOid) {
    this.ifNameOid = ifNameOid;
  }

  public String getSnmptrapIfNameOid() {
    return snmptrapIfNameOid;
  }

  public void setSnmptrapIfNameOid(String snmptrapIfNameOid) {
    this.snmptrapIfNameOid = snmptrapIfNameOid;
  }

  public Integer getMaxRepetitions() {
    return maxRepetitions;
  }

  public void setMaxRepetitions(Integer maxRepetitions) {
    this.maxRepetitions = maxRepetitions;
  }

  public List<IfNameRuleEcEntity> getIfNameRuleList() {
    return ifNameRuleList;
  }

  public void setIfNameRuleList(List<IfNameRuleEcEntity> ifNameRuleList) {
    this.ifNameRuleList = ifNameRuleList;
  }

  public List<EquipmentIfEcEntity> getEquipmentIfList() {
    return equipmentIfList;
  }

  public void setEquipmentIfList(List<EquipmentIfEcEntity> equipmentIfList) {
    this.equipmentIfList = equipmentIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
