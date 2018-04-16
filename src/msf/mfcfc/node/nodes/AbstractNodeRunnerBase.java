
package msf.mfcfc.node.nodes;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractAsyncRunner;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement the common process of node-related asynchronous
 * processing in configuration management function.
 *
 * @author NTT
 *
 */
public abstract class AbstractNodeRunnerBase extends AbstractAsyncRunner {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractNodeRunnerBase.class);

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }
}
