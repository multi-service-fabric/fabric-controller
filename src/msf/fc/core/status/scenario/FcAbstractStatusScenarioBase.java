
package msf.fc.core.status.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.AbstractStatusScenarioBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement common process of system status-related
 * processing in system basic function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractStatusScenarioBase<T extends RestRequestBase> extends AbstractStatusScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractStatusScenarioBase.class);

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

}
