
package msf.mfcfc.services.ctrlstsnotify.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement the common process of controller status-related
 * processing in the extension functions.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class AbstractCtrlStsNotifyScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractCtrlStsNotifyScenarioBase.class);

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase responseNotifyData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
