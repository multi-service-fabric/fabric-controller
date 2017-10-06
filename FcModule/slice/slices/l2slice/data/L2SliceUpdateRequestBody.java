package msf.fc.slice.slices.l2slice.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.SliceUpdateAction;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.rest.common.RestRequestValidator;

public class L2SliceUpdateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceUpdateRequestBody.class);
  @SerializedName("action")
  private String action;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public SliceUpdateAction getActionEnum() {
    return SliceUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(SliceUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      if (getActionEnum() == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "action", action);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
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
