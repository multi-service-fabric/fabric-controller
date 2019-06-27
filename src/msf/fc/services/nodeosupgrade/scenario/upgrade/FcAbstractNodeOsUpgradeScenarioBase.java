
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.AbstractNodeOsUpgradeScenarioBase;

/**
 * Abstract class to implement the common process for the node OS upgrade
 * function main process in the FC.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractNodeOsUpgradeScenarioBase<T extends RestRequestBase>
    extends AbstractNodeOsUpgradeScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractNodeOsUpgradeScenarioBase.class);

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
