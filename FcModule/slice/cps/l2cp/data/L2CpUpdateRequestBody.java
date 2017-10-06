package msf.fc.slice.cps.l2cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.CpUpdateAction;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.rest.common.RestRequestValidator;

public class L2CpUpdateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public CpUpdateAction getActionEnum() {
    return CpUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(CpUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      CpUpdateAction cpUpdateAction = getActionEnum();
      if (cpUpdateAction == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "action", action);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

      if (CpUpdateAction.FORCE_DELETE.equals(cpUpdateAction)) {
        String logMsg = MessageFormat.format("param is not target.param = {0}, value = {1}", "action", action);
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
