
package msf.mfcfc.core.status.scenario.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestRequestValidator;


public class SystemStatusUpdateBody implements RestRequestValidator {
  
  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusUpdateBody.class);

  
  @SerializedName("service_status")
  private String serviceStatus;

  
  @SerializedName("blockade_status")
  private String blockadeStatus;

  
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

  
  public ServiceStatus getServiceStatusEnum() {
    return ServiceStatus.getEnumFromMessage(serviceStatus);
  }

  
  public void setServiceStatusEnum(ServiceStatus serviceStatus) {
    this.serviceStatus = serviceStatus.getMessage();
  }

  
  public BlockadeStatus getBlockadeStatusEnum() {
    return BlockadeStatus.getEnumFromMessage(blockadeStatus);
  }

  
  public void setBlockadeStatusEnum(BlockadeStatus blockadeStatus) {
    this.blockadeStatus = blockadeStatus.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();


      if (serviceStatus == null) {
        logger.debug("serviceStatus is null.");
      } else {


        if (getServiceStatusEnum() == null) {
          String errorMessage = "serivice_status is unknown messssage. (service_status={0})";
          errorMessage = MessageFormat.format(errorMessage, serviceStatus);

          logger.debug(errorMessage);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, errorMessage);
        }
      }



      if (blockadeStatus == null) {
        logger.debug("blockadeStatus is null.");
      } else {


        if (getBlockadeStatusEnum() == null) {
          String errorMessage = "blockade_status is unknown messssage. (blockade_status={0})";
          errorMessage = MessageFormat.format(errorMessage, blockadeStatus);

          logger.debug(errorMessage);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, errorMessage);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
