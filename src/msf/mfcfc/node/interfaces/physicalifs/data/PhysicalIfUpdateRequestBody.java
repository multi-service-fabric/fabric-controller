
package msf.mfcfc.node.interfaces.physicalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PhysicalIfUpdateAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;

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

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public PhysicalIfUpdateAction getActionEnum() {
    return PhysicalIfUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(PhysicalIfUpdateAction action) {
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
