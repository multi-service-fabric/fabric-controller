
package msf.mfc.node.interfaces;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.AbstractInterfaceRunnerBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement the common process of interface-related
 * asynchronous processing in configuration management function.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractInterfaceRunnerBase extends AbstractInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractInterfaceRunnerBase.class);

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcSwCluster getSwCluster(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao,
      Integer swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao", "swClusterId" },
          new Object[] { mfcSwClusterDao, swClusterId });
      MfcSwCluster mfcSwCluster = mfcSwClusterDao.read(sessionWrapper, swClusterId);
      if (mfcSwCluster == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
      return mfcSwCluster;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponseDataFromAsyncRequestsForLower(AsyncRequestsForLower asyncRequestsForLower) {
    try {
      logger.methodStart(new String[] { "asyncRequestsForLower" }, new Object[] { asyncRequestsForLower });
      return new RestResponseBase(asyncRequestsForLower.getResponseStatusCode(),
          asyncRequestsForLower.getResponseBody());
    } finally {
      logger.methodEnd();
    }
  }

}