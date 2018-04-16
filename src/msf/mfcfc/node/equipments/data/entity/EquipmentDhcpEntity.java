
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentDhcpEntity {

  @SerializedName("dhcp_template")
  private String dhcpTemplate;

  @SerializedName("config_template")
  private String configTemplate;

  @SerializedName("initial_config")
  private String initialConfig;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
