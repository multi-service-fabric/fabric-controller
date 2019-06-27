
package msf.fc.services.priorityroutes.scenario.nodes;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.priorityroutes.scenario.nodes.AbstractNodeGroupPriorityScenarioBase;

/**
 * Abstract class to implement the priority node group related common process in
 * the priority routes control management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits RestRequestBase class
 */
public abstract class FcAbstractNodeGroupPriorityScenarioBase<T extends RestRequestBase>
    extends AbstractNodeGroupPriorityScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractNodeGroupPriorityScenarioBase.class);

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
