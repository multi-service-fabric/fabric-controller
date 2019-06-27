
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OsUpgradeEntity {

  @SerializedName("upgrade_script_path")
  private String upgradeScriptPath;

  @SerializedName("ztp_flag")
  private Boolean ztpFlag;

  @SerializedName("upgrade_complete_msg")
  private String upgradeCompleteMsg;

  @SerializedName("upgrade_error_msgs")
  private List<String> upgradeErrorMsgList;

  public String getUpgradeScriptPath() {
    return upgradeScriptPath;
  }

  public void setUpgradeScriptPath(String upgradeScriptPath) {
    this.upgradeScriptPath = upgradeScriptPath;
  }

  public Boolean getZtpFlag() {
    return ztpFlag;
  }

  public void setZtpFlag(Boolean ztpFlag) {
    this.ztpFlag = ztpFlag;
  }

  public String getUpgradeCompleteMsg() {
    return upgradeCompleteMsg;
  }

  public void setUpgradeCompleteMsg(String upgradeCompleteMsg) {
    this.upgradeCompleteMsg = upgradeCompleteMsg;
  }

  public List<String> getUpgradeErrorMsgList() {
    return upgradeErrorMsgList;
  }

  public void setUpgradeErrorMsgList(List<String> upgradeErrorMsgList) {
    this.upgradeErrorMsgList = upgradeErrorMsgList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
