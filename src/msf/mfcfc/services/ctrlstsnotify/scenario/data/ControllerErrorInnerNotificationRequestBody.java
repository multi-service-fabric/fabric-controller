
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.ctrlstsnotify.common.constant.SystemType;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerErrorInnerNotificationEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.DeviceListEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.FailureInfoEntity;

public class ControllerErrorInnerNotificationRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(ControllerErrorInnerNotificationRequestBody.class);

  @SerializedName("controller")
  private ControllerErrorInnerNotificationEntity controller;

  public ControllerErrorInnerNotificationEntity getController() {
    return controller;
  }

  public void setController(ControllerErrorInnerNotificationEntity controller) {
    this.controller = controller;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(controller);

      ParameterCheckUtil.checkNotNull(controller.getControllerTypeEnum());

      ParameterCheckUtil.checkNotNull(controller.getSystemTypeEnum());

      ParameterCheckUtil.checkNotNull(controller.getFailureInfo());
      validateFailureInfo();
    } finally {
      logger.methodEnd();
    }
  }

  private void validateFailureInfo() throws MsfException {
    FailureInfoEntity failureInfo = controller.getFailureInfo();
    int checker = 0;

    if (failureInfo.getCpu() != null) {

      ParameterCheckUtil.checkNotNull(failureInfo.getCpu().getUseRate());
      checker++;
    }

    if (failureInfo.getMemory() != null) {

      ParameterCheckUtil.checkNotNull(failureInfo.getMemory().getUsed());

      if (controller.getSystemTypeEnum().equals(SystemType.OPERATING_SYSTEM)) {

        ParameterCheckUtil.checkNotNull(failureInfo.getMemory().getFree());
      }
      checker++;
    }

    if (failureInfo.getDisk() != null) {

      ParameterCheckUtil.checkNotNull(failureInfo.getDisk().getDeviceList());
      validateDeviceList();
      checker++;
    }

    if (checker == 0) {
      String logMsg = "parameter to failureInfo is not set.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, logMsg);
    }
  }

  private void validateDeviceList() throws MsfException {
    for (DeviceListEntity deviceList : controller.getFailureInfo().getDisk().getDeviceList()) {

      ParameterCheckUtil.checkNotNullAndLength(deviceList.getFileSystem());

      ParameterCheckUtil.checkNotNullAndLength(deviceList.getMountedOn());

      ParameterCheckUtil.checkNotNull(deviceList.getSize());

      ParameterCheckUtil.checkNotNull(deviceList.getUsed());
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
