package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class EquipmentSnmpEntity {

  
  @SerializedName("if_name_oid")
  private String ifNameOid;

  
  @SerializedName("snmptrap_if_name_oid")
  private String snmptrapIfNameOid;

  
  @SerializedName("max_repetitions")
  private Integer maxRepetitions;

  
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

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
