
package msf.mfcfc.services.nodeosupgrade.scenario.detour.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeDetourUpdateAction;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.entity.NodeDetourUpdateOptionEntity;

public class NodeDetourUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeDetourUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("update_option")
  private NodeDetourUpdateOptionEntity updateOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public NodeDetourUpdateOptionEntity getUpdateOption() {
    return updateOption;
  }

  public void setUpdateOption(NodeDetourUpdateOptionEntity updateOption) {
    this.updateOption = updateOption;
  }

  public NodeDetourUpdateAction getActionEnum() {
    return NodeDetourUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(NodeDetourUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());

      switch (getActionEnum()) {
        case UPDATE:

          ParameterCheckUtil.checkNotNull(updateOption);

          ParameterCheckUtil.checkNotNull(updateOption.getDetoured());
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("action={0}", getActionEnum()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
