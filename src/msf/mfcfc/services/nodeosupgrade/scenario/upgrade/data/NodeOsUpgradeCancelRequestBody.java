
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.nodeosupgrade.common.constant.UpgradeCancelType;

public class NodeOsUpgradeCancelRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeOsUpgradeCancelRequestBody.class);

  @SerializedName("action")
  private String action;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public UpgradeCancelType getActionEnum() {
    return UpgradeCancelType.getEnumFromMessage(action);
  }

  public void setActionEnum(UpgradeCancelType action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());
    } finally {
      logger.methodEnd();
    }

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
