package msf.fc.core.status.scenario.data;

import java.text.MessageFormat;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.BlockadeStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.ServiceStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.rest.common.RestRequestValidator;

public class SystemStatusUpdateBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusUpdateBody.class);

  @SerializedName("service_status")
  private String serviceStatus;

  @SerializedName("blockade_status")
  private String blockadeStatus;

  @Override
  public void validate() throws MsfException {
    logger.methodStart();

    if (serviceStatus == null) {
      logger.debug("serviceStatus is null.");
    } else {

      if (ServiceStatus.getEnumFromMessage(serviceStatus) == null) {
        String errorMessage = "serivice_status is unknown messssage. (service_status={0})";
        errorMessage = MessageFormat.format(errorMessage, serviceStatus);

        logger.debug(errorMessage);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, errorMessage);
      }
    }

    if (blockadeStatus == null) {
      logger.debug("blockadeStatus is null.");
    } else {

      if (BlockadeStatus.getEnumFromMessage(blockadeStatus) == null) {
        String errorMessage = "blockade_status is unknown messssage. (blockade_status={0})";
        errorMessage = MessageFormat.format(errorMessage, blockadeStatus);

        logger.debug(errorMessage);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, errorMessage);
      }
    }

    logger.methodEnd();
  }

  public String getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(String serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public String getBlockadeStatus() {
    return blockadeStatus;
  }

  public void setBlockadeStatus(String blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  @Override
  public String toString() {
    return "SystemStatusUpdateBody [serviceStatus=" + serviceStatus + ", blockadeStatus=" + blockadeStatus + "]";
  }

}
