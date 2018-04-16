
package msf.mfc.traffic.traffics;

import java.text.MessageFormat;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.traffic.traffics.AbstractIfTrafficScenarioBase;
import msf.mfcfc.traffic.traffics.data.IfTrafficRequest;

/**
 * Abstract class to implement the common process of IF traffic information
 * acquisition processing in traffic management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractIfTrafficScenarioBase<T extends RestRequestBase>
    extends AbstractIfTrafficScenarioBase<T> {

  protected IfTrafficRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractIfTrafficScenarioBase.class);

  protected MfcSwCluster getMfcSwCluster(SessionWrapper session) throws MsfException {

    try {

      logger.methodStart(new String[] { "session" }, new Object[] { session });
      MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

      MfcSwCluster mfcSwCluster = mfcSwClusterDao.read(session, Integer.parseInt(request.getClusterId()));

      if (mfcSwCluster == null) {

        String logMsg = MessageFormat.format("target resource not found. parameters={0}, clusterId={1}", "cluster",
            request.getClusterId());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }

      return mfcSwCluster;

    } finally {
      logger.methodEnd();
    }

  }

  protected RestResponseBase sendTrafficRead() throws MsfException {

    try {
      logger.methodStart();

      String requestUri = MfcFcRequestUri.IF_TRAFFIC_READ.getUri(request.getClusterId(), request.getFabricType(),
          request.getNodeId(), request.getIfType(), request.getIfId());

      return sendTrafficRequest(MfcFcRequestUri.IF_TRAFFIC_READ.getHttpMethod(), requestUri);

    } finally {
      logger.methodEnd();
    }

  }

  protected RestResponseBase sendTrafficReadList() throws MsfException {

    try {
      logger.methodStart();

      String requestUri = MfcFcRequestUri.IF_TRAFFIC_READ_LIST.getUri(request.getClusterId(), request.getFabricType(),
          request.getNodeId());

      return sendTrafficRequest(MfcFcRequestUri.IF_TRAFFIC_READ_LIST.getHttpMethod(), requestUri);

    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase sendTrafficRequest(HttpMethod method, String uri) throws MsfException {
    try {
      logger.methodStart();

      String fcControlIpAddress = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.parseInt(request.getClusterId())).getSwCluster().getFcControlAddress();
      int fcControlPort = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.parseInt(request.getClusterId())).getSwCluster().getFcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(method, uri, null, fcControlIpAddress, fcControlPort);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }

  }

}
