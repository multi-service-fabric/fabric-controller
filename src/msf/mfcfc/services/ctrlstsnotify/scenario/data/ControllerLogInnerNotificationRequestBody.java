
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerLogInnerNotificationEntity;

public class ControllerLogInnerNotificationRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(ControllerLogInnerNotificationRequestBody.class);

  @SerializedName("controller")
  private ControllerLogInnerNotificationEntity controller;

  public ControllerLogInnerNotificationEntity getController() {
    return controller;
  }

  public void setController(ControllerLogInnerNotificationEntity controller) {
    this.controller = controller;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(controller);

      ParameterCheckUtil.checkNotNull(controller.getControllerTypeEnum());

      ParameterCheckUtil.checkNotNull(controller.getLogLevelEnum());

      ParameterCheckUtil.checkNotNullAndLength(controller.getLogList());

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
