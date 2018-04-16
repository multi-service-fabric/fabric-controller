
package msf.mfcfc.core.status.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerInnerNotifyEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class SystemStatusInnerNotifyRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusInnerNotifyRequestBody.class);

  @SerializedName("controller")
  private SystemStatusControllerInnerNotifyEntity controller;

  public SystemStatusControllerInnerNotifyEntity getController() {
    return controller;
  }

  public void setController(SystemStatusControllerInnerNotifyEntity controller) {
    this.controller = controller;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(controller);

      validateController();

    } finally {
      logger.methodEnd();
    }
  }

  private void validateController() throws MsfException {

    ParameterCheckUtil.checkNotNull(controller.getControllerTypeEnum());

    ParameterCheckUtil.checkNotNull(controller.getControllerEventEnum());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
