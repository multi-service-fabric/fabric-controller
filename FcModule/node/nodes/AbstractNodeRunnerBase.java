package msf.fc.node.nodes;

import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.EcEmControlStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.AbstractAsyncRunner;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.AbstractResponseBody;

public abstract class AbstractNodeRunnerBase extends AbstractAsyncRunner {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractNodeRunnerBase.class);

  protected static final String LOOPBACK_INTERFACE_PREFIX = "32";
  protected static final Integer LAG_INTERFACE_PREFIX = 30;

  private static Pattern ecErrorRollBackPattern = Pattern.compile("^80[0-9]{4}$");

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkEcControlError(ErrorCode ecResponseStatus, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecResponseStatus", "node" }, new Object[] { ecResponseStatus, node });
      if (ecResponseStatus != null) {
        switch (ecResponseStatus) {
          case EC_CONTROL_TIMEOUT:
            node.setProvisioningStatusEnum(ProvisioningStatus.BOOT_FAILED);
            break;
          case EC_CONTROL_ERROR_EM_CONTROL_COMPLETED:
            node.setProvisioningStatusEnum(ProvisioningStatus.BOOT_COMPLETE);
            break;
          default:
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, "UNDEFINED");
        }
      } else {
        node.setProvisioningStatusEnum(ProvisioningStatus.BOOT_COMPLETE);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeDeleteEcError(ErrorCode ecResponseStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecResponseStatus" }, new Object[] { ecResponseStatus });
      if (ecResponseStatus != null) {
        String errorMsg = "Check Node Delete EC Error.";
        switch (ecResponseStatus) {
          case EC_CONTROL_ERROR:
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
          case EC_CONTROL_TIMEOUT:
            throw new MsfException(ErrorCode.EC_CONTROL_TIMEOUT, errorMsg);
          case EC_CONTROL_ERROR_EM_CONTROL_COMPLETED:
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, errorMsg);
          default:
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeNotifyEcError(ErrorCode ecResponseStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecResponseStatus" }, new Object[] { ecResponseStatus });
      if (ecResponseStatus != null) {
        String errorMsg = "Check Node Notify EC Error.";
        switch (ecResponseStatus) {
          case EC_CONNECTION_ERROR:
            throw new MsfException(ErrorCode.EC_CONNECTION_ERROR, errorMsg);
          case EC_CONTROL_ERROR:
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
          case EC_CONTROL_TIMEOUT:
            throw new MsfException(ErrorCode.EC_CONTROL_TIMEOUT, errorMsg);
          default:
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  public void commitErrorAfterEcControl(EcEmControlStatus status) throws MsfException {
    try {
      logger.methodStart(new String[] { "status" }, new Object[] { status });
      String errorMsg = "Error commit Transaction.";
      switch (status) {
        case EM_SUCCESS_BUT_EC_FAILED:
          throw new MsfException(ErrorCode.FC_EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, errorMsg);

        case SUCCESS:
          throw new MsfException(ErrorCode.FC_CONTROL_ERROR_EC_EM_CONTROL_COMPLETED, errorMsg);

        case FAILED:
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);

        default:
          throw new IllegalArgumentException(errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public static ErrorCode checkEcEmControlErrorCodeAfterNodeDelete(String ecErrorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecErrorCode" }, new Object[] { ecErrorCode });
      if (ecErrorCode != null && !ecErrorCode.isEmpty()) {
        if (ecErrorRollBackPattern.matcher(ecErrorCode).find()) {
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, "Roll back by EC control error.");
        } else {
          return ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }
}
