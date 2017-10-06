package msf.fc.rest.ec.node.nodes.spine.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentEcEntity {
  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("config_template")
  private String configTemplate;

  @SerializedName("initial_config")
  private String initialConfig;
  @SerializedName("boot_complete_msg")
  private String bootCompleteMsg;
  @SerializedName("boot_error_msgs")
  private List<String> bootErrorMsgs;

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getConfigTemplate() {
    return configTemplate;
  }

  public void setConfigTemplate(String configTemplate) {
    this.configTemplate = configTemplate;
  }

  public String getInitialConfig() {
    return initialConfig;
  }

  public void setInitialConfig(String initialConfig) {
    this.initialConfig = initialConfig;
  }

  public String getBootCompleteMsg() {
    return bootCompleteMsg;
  }

  public void setBootCompleteMsg(String bootCompleteMsg) {
    this.bootCompleteMsg = bootCompleteMsg;
  }

  public List<String> getBootErrorMsgs() {
    return bootErrorMsgs;
  }

  public void setBootErrorMsgs(List<String> bootErrorMsgs) {
    this.bootErrorMsgs = bootErrorMsgs;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
