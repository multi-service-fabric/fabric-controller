package msf.fc.node.interfaces.physicalifs.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.PhysicalIfUpdateAction;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class PhysicalIfUpdateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(PhysicalIfUpdateRequestBody.class);
  @SerializedName("action")
  private String action;

  @SerializedName("speed")
  private String speed;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public PhysicalIfUpdateAction getActionEnum() {
    return PhysicalIfUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(PhysicalIfUpdateAction action) {
    this.action = action.getMessage();
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  @Override
  public void validate() throws MsfException {

    PhysicalIfUpdateAction physicalIfUpdateAction = getActionEnum();
    ParameterCheckUtil.checkNotNull(physicalIfUpdateAction);

    if (PhysicalIfUpdateAction.BREAKOUT_IF_CREATE.equals(physicalIfUpdateAction)
        || PhysicalIfUpdateAction.BREAKOUT_IF_DELETE.equals(physicalIfUpdateAction)) {
      String logMsg = MessageFormat.format("param is not target.param = {0}, value = {1}", "action", action);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
