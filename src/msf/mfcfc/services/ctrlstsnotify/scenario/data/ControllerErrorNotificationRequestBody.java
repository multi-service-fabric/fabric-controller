
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerErrorNotificationEntity;

public class ControllerErrorNotificationRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(ControllerErrorNotificationRequestBody.class);

  @SerializedName("controller")
  private ControllerErrorNotificationEntity controller;

  public ControllerErrorNotificationEntity getController() {
    return controller;
  }

  public void setController(ControllerErrorNotificationEntity controller) {
    this.controller = controller;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
