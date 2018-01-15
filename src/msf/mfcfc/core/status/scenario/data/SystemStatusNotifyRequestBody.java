package msf.mfcfc.core.status.scenario.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerNotifyEntity;
import msf.mfcfc.rest.common.RestRequestValidator;


public class SystemStatusNotifyRequestBody implements RestRequestValidator {
  
  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusNotifyRequestBody.class);
  
  @SerializedName("controller")
  private SystemStatusControllerNotifyEntity controller;

  
  public SystemStatusControllerNotifyEntity getController() {
    return controller;
  }

  
  public void setController(SystemStatusControllerNotifyEntity controller) {
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

    switch (controller.getControllerTypeEnum()) {
      case EC:
      case EM:
      case FC:

        ParameterCheckUtil.checkNotNullAndLength(controller.getClusterId());
        break;
      case MFC:
        break;
      default:

        throw new IllegalArgumentException(
            MessageFormat.format("controllerType={0}", controller.getControllerTypeEnum()));
    }

    ParameterCheckUtil.checkNotNull(controller.getControllerEventEnum());
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
