package msf.fc.common.internal.data;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.InternalOperationAction;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class InternalOperationRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(InternalOperationRequestBody.class);
  @SerializedName("action")
  private String action;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Override
  public void validate() throws MsfException {
    logger.methodStart();
    try {
      ParameterCheckUtil.checkNotNull(InternalOperationAction.getEnumFromMessage(action));
    } finally {
      logger.methodEnd();
    }
  }

}
