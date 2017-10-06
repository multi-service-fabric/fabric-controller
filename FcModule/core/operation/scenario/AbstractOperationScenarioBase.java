package msf.fc.core.operation.scenario;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.operation.scenario.data.OperationRequest;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;

public abstract class AbstractOperationScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractOperationScenarioBase.class);

  protected OperationRequest request;

  protected final SimpleDateFormat writeDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

  protected void checkFormatOption(String format) throws MsfException {
    try {
      logger.methodStart();
      RestFormatOption formatEnum = RestFormatOption.getEnumFromMessage(format);
      if (formatEnum == null) {
        String logMsg = MessageFormat.format("Format is undefined.(format={0})", format);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
