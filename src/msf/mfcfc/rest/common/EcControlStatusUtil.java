
package msf.mfcfc.rest.common;

import java.util.regex.Pattern;

import msf.mfcfc.common.constant.EcEmControlStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.log.MsfLogger;

public class EcControlStatusUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(EcControlStatusUtil.class);

  private static Pattern errorPattern = Pattern.compile("^90[0-9]{4}$");

  public static EcEmControlStatus getStatusFromEcErrorCode(String ecErrorCode) {
    try {
      logger.methodStart(new String[] { "ecErrorCode" }, new Object[] { ecErrorCode });
      if (ecErrorCode != null && !ecErrorCode.isEmpty()) {

        if (errorPattern.matcher(ecErrorCode).find()) {
          return EcEmControlStatus.EM_SUCCESS_BUT_EC_FAILED;
        } else {
          return EcEmControlStatus.FAILED;
        }
      }
      return EcEmControlStatus.SUCCESS;
    } finally {
      logger.methodEnd();
    }
  }

  public static ErrorCode checkEcEmControlErrorCode(String ecErrorCode) {
    try {
      logger.methodStart(new String[] { "ecErrorCode" }, new Object[] { ecErrorCode });
      if (ecErrorCode != null && !ecErrorCode.isEmpty()) {

        if (errorPattern.matcher(ecErrorCode).find()) {
          return ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED;
        } else {
          return ErrorCode.EC_CONTROL_ERROR;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  public static EcEmControlStatus getStatusFromFcErrorCode(ErrorCode errorCode) {
    try {
      logger.methodStart(new String[] { "errorCode" }, new Object[] { errorCode });
      if (errorCode != null) {
        switch (errorCode) {
          case EC_CONTROL_ERROR_EM_CONTROL_COMPLETED:
            return EcEmControlStatus.EM_SUCCESS_BUT_EC_FAILED;
          case EC_CONNECTION_ERROR:
          case EC_CONTROL_TIMEOUT:
          case EC_CONTROL_ERROR:
          default:
            return EcEmControlStatus.FAILED;
        }
      }
      return EcEmControlStatus.SUCCESS;
    } finally {
      logger.methodEnd();
    }
  }
}
