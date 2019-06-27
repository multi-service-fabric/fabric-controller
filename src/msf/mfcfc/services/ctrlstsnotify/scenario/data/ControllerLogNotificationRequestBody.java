
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerLogNotificationEntity;

public class ControllerLogNotificationRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(ControllerLogNotificationRequestBody.class);

  @SerializedName("controller")
  private ControllerLogNotificationEntity controller;

  public ControllerLogNotificationEntity getController() {
    return controller;
  }

  public void setController(ControllerLogNotificationEntity controller) {
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
