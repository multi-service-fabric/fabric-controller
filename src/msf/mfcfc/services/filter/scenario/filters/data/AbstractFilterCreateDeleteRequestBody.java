
package msf.mfcfc.services.filter.scenario.filters.data;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.services.filter.common.constant.FilterAction;
import msf.mfcfc.services.filter.common.constant.FilterDirection;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterValueEntity;

public class AbstractFilterCreateDeleteRequestBody {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractFilterCreateDeleteRequestBody.class);

  public PatchOperation getOpEnum(String op) throws MsfException {
    PatchOperation operation = PatchOperation.getEnumFromMessage(op);
    if (operation == null) {
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "Illegal parameter");
    } else {
      return operation;
    }
  }

  public void validate(String op, String path, FilterValueEntity value) throws MsfException {
    try {
      String logMsg = "Illegal parameter";
      logger.methodStart();

      ParameterCheckUtil.checkNotNullAndLength(op);
      switch (getOpEnum(op)) {
        case ADD:
          ParameterCheckUtil.checkNotNull(value);
          validateValue(value);
          if (FilterAction.getEnumFromMessage(value.getAction()) != FilterAction.DISCARD) {
            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
          }

          if (FilterDirection.getEnumFromDirection(value.getDirection()) != FilterDirection.IN) {
            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
          }
          break;
        case REMOVE:
          if (value != null) {
            logger.error(logMsg);
            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
          }

          ParameterCheckUtil.checkNotNullAndLength(path);
          break;
        default:
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateValue(FilterValueEntity value) throws MsfException {
    String[] strArray = { value.getSourceMacAddress(), value.getDestMacAddress(), value.getSourceIpAddress(),
        value.getDestIpAddress(), value.getProtocol(), value.getSourcePort(), value.getDestPort() };
    int flag = 0;

    for (int i = 0; i < strArray.length; i++) {
      if (strArray[i] == null || strArray[i].isEmpty()) {
        continue;
      }
      flag++;
    }
    if (flag == 0) {
      String logMsg = "At least one value must be set for the parameter under \"value\".";
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }
  }
}
