package msf.fc.node.interfaces;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.AbstractAsyncRunner;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.AbstractResponseBody;

public abstract class AbstractInterfaceRunnerBase extends AbstractAsyncRunner {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractInterfaceRunnerBase.class);

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
