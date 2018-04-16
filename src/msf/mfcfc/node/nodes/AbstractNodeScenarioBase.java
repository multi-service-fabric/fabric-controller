
package msf.mfcfc.node.nodes;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement the common process of node-related processing in
 * configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractNodeScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractNodeScenarioBase.class);

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body", "statusCode" },
          new Object[] { ToStringBuilder.reflectionToString(body), statusCode });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkUserTypeOperator(RestUserTypeOption userType) throws MsfException {
    try {
      logger.methodStart();
      if (!RestUserTypeOption.OPERATOR.equals(userType)) {
        String logMsg = "user-type is not operator";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
