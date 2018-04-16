
package msf.fc.rest.ec.node.equipment.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentZtpEcEntity {

  @SerializedName("dhcp_template")
  private String dhcpTemplate;

  @SerializedName("config_template")
  private String configTemplate;

  @SerializedName("initial_config")
  private String initialConfig;

  @SerializedName("boot_complete_msg")
  private String bootCompleteMsg;

  @SerializedName("boot_error_msgs")
  private List<String> bootErrorMsgList;

  public String getDhcpTemplate() {
    return dhcpTemplate;
  }

  public void setDhcpTemplate(String dhcpTemplate) {
    this.dhcpTemplate = dhcpTemplate;
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

  public List<String> getBootErrorMsgList() {
    return bootErrorMsgList;
  }

  public void setBootErrorMsgList(List<String> bootErrorMsgList) {
    this.bootErrorMsgList = bootErrorMsgList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
