
package msf.mfcfc.failure.logicalif.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InternalOperationAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusIfEntity;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusNodeEntity;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfUpdateLogicalIfStatusOptionEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class LogicalIfOperationRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(LogicalIfOperationRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("update_logical_if_status_option")
  private LogicalIfUpdateLogicalIfStatusOptionEntity updateLogicalIfStatusOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public LogicalIfUpdateLogicalIfStatusOptionEntity getUpdateLogicalIfStatusOption() {
    return updateLogicalIfStatusOption;
  }

  public void setUpdateLogicalIfStatusOption(LogicalIfUpdateLogicalIfStatusOptionEntity updateLogicalIfStatusOption) {
    this.updateLogicalIfStatusOption = updateLogicalIfStatusOption;
  }

  public InternalOperationAction getActionEnum() {
    return InternalOperationAction.getEnumFromMessage(action);
  }

  public void setActionEnum(InternalOperationAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());

      switch (getActionEnum()) {
        case UPDATE_LOGICAL_IF_STATUS:

          ParameterCheckUtil.checkNotNull(updateLogicalIfStatusOption);
          validateUpdateLogicalIfStatusOption();
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("status={0}", getActionEnum()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateUpdateLogicalIfStatusOption() throws MsfException {

    if (updateLogicalIfStatusOption.getNodeList() != null) {
      validateNodeList();
    }
    if (updateLogicalIfStatusOption.getIfList() != null) {
      validateIfList();
    }
  }

  private void validateIfList() throws MsfException {

    for (LogicalIfStatusIfEntity tempIf : updateLogicalIfStatusOption.getIfList()) {
      validateIf(tempIf);
    }
  }

  private void validateNodeList() throws MsfException {

    for (LogicalIfStatusNodeEntity tempNode : updateLogicalIfStatusOption.getNodeList()) {
      validateNode(tempNode);
    }
  }

  private void validateNode(LogicalIfStatusNodeEntity tempNode) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(tempNode.getNodeId());

    ParameterCheckUtil.checkNotNull(tempNode.getFailureStatusEnum());
  }

  private void validateIf(LogicalIfStatusIfEntity tempIf) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getNodeId());

    ParameterCheckUtil.checkNotNull(tempIf.getIfTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getIfId());

    ParameterCheckUtil.checkNotNull(tempIf.getStatusEnum());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
